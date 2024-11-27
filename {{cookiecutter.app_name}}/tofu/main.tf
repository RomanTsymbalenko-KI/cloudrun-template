# VPC Network
resource "google_compute_network" "vpc_network" {
  name                    = "${var.cloud_run_service_name}-vpc-network"
  auto_create_subnetworks = "true"
}

# Cloud Run Service Identity
resource "google_service_account" "cloudrun_service_identity" {
  account_id   = "cloud-run-service-identity"
  display_name = "Cloud Run Service Identity"
}

# Project IAM
resource "google_project_iam_binding" "project" {
  project = var.gcp_project_id
  role    = "roles/owner"
  members = concat(["serviceAccount:ki-cicd@${var.gcp_project_id}.iam.gserviceaccount.com"], var.gcp_project_iam_owners)
}

resource "google_project_iam_binding" "metric_writer_user_role" {
  project = var.gcp_project_id
  role    = "roles/monitoring.metricWriter"
  members = ["serviceAccount:${google_service_account.cloudrun_service_identity.email}"]
}

resource "google_project_iam_binding" "log_writer_user_role" {
  project = var.gcp_project_id
  role    = "roles/logging.logWriter"
  members = ["serviceAccount:${google_service_account.cloudrun_service_identity.email}"]
}

resource "google_storage_bucket" "metrics_config_bucket" {
  name                        = "metrics-config-bucket-${var.gcp_project_id}"
  location                    = var.region
  uniform_bucket_level_access = true
}

resource "google_storage_bucket_object" "object" {
  name    = "config.yaml"
  bucket  = google_storage_bucket.metrics_config_bucket.name
  content = replace(file("otel-collector-config.yaml"), "MONITORING_PROJECT_ID", var.monitoring_project_id)
}

resource "google_storage_bucket_iam_binding" "binding" {
  bucket  = google_storage_bucket.metrics_config_bucket.name
  role    = "roles/storage.objectViewer"
  members = ["serviceAccount:${google_service_account.cloudrun_service_identity.email}"]
}
