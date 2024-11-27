# Provisions a Postgres instance with a private IP.
# Based on https://cloud.google.com/sql/docs/postgres/configure-private-ip#terraform
# Note that the VPC network resource is declared in /terraform/main.tf so its ID can be shared between the Cloud Run Service and the Cloud SQL DB Instance.

resource "google_compute_global_address" "private_ip_address" {
  name          = "${var.db_instance_name}-private-ip-address"
  purpose       = "VPC_PEERING"
  address_type  = "INTERNAL"
  prefix_length = 16
  network       = var.vpc_network_id
}

resource "google_service_networking_connection" "default" {
  network                 = var.vpc_network_id
  service                 = "servicenetworking.googleapis.com"
  reserved_peering_ranges = [google_compute_global_address.private_ip_address.name]
}

resource "google_sql_database_instance" "default" {
  name             = var.db_instance_name
  region           = var.region
  database_version = var.db_version

  depends_on = [google_service_networking_connection.default]

  settings {
    tier = var.db_tier
    ip_configuration {
      ipv4_enabled    = "false"
      private_network = var.vpc_network_id
    }
  }
  # set `deletion_protection` to true, will ensure that one cannot accidentally delete this instance by
  # use of Terraform whereas `deletion_protection_enabled` flag protects this instance at the GCP level.
  deletion_protection = false
}

resource "google_sql_database" "default" {
  name      = var.db_name
  instance  = google_sql_database_instance.default.name
  charset   = "UTF8"
  collation = "en_US.UTF8"
}

resource "google_compute_network_peering_routes_config" "peering_routes" {
  peering              = google_service_networking_connection.default.peering
  network              = var.vpc_network_name
  import_custom_routes = true
  export_custom_routes = true
}

resource "random_password" "root_user_password" {
  length           = 64
  special          = true
  override_special = "_%@"
}

# We must create a new root user as the default one created on instance creation will be deleted.
# https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/sql_database_instance
resource "google_sql_user" "root" {
  name     = "root"
  instance = google_sql_database_instance.default.name
  password = random_password.root_user_password.result
}

resource "google_secret_manager_secret" "root_user_password_secret" {
  secret_id = "postgres_root_user_pwd"
  replication {
    user_managed {
      replicas {
        location = var.region
      }
    }
  }
}

resource "google_secret_manager_secret_version" "root_user_password_secret_version" {
  secret      = google_secret_manager_secret.root_user_password_secret.id
  secret_data = random_password.root_user_password.result
}

resource "google_secret_manager_secret_iam_member" "root_user_password_secret_iam_role" {
  secret_id  = google_secret_manager_secret.root_user_password_secret.id
  role       = "roles/secretmanager.secretAccessor"
  member     = "serviceAccount:${var.application_user_service_account_email}"
  depends_on = [google_secret_manager_secret.root_user_password_secret]
}
