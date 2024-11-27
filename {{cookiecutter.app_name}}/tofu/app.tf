resource "google_cloud_run_v2_service" "default" {
  name     = var.cloud_run_service_name
  location = var.region
  ingress  = "INGRESS_TRAFFIC_ALL"

  template {
    scaling {
      min_instance_count = 1
      max_instance_count = 2
    }

    service_account = google_service_account.cloudrun_service_identity.email

    volumes {
      name = "otel-config-volume"
      gcs {
        bucket    = google_storage_bucket.metrics_config_bucket.name
        read_only = true
      }
    }

    containers {
      image = var.cloud_run_container_image

      ports {
        container_port = "8080"
      }

      resources {
        limits = {
          "cpu"    = "1000m"
          "memory" = "512Mi"
        }
        cpu_idle = false # Ensure CPU is always allocated
      }
    }

    containers {
      image = "us-docker.pkg.dev/cloud-ops-agents-artifacts/cloud-run-gmp-sidecar/cloud-run-gmp-sidecar:1.1.1"
      name  = "collector"
      env {
        name  = "GCP_PROJECT"
        value = var.monitoring_project_id
      }
      resources {
        limits = {
          "cpu"    = "1000m"
          "memory" = "512Mi"
        }
        cpu_idle = false # Ensure CPU is always allocated
      }
    }

    containers {
      image = "otel/opentelemetry-collector-contrib:0.112.0"
      name  = "otel-collector"
      resources {
        limits = {
          "cpu"    = "1000m"
          "memory" = "512Mi"
        }
        cpu_idle = false # Ensure CPU is always allocated
      }
      volume_mounts {
        name       = "otel-config-volume"
        mount_path = "/etc/otelcol-contrib"
      }
    }

    vpc_access {
      network_interfaces {
        network = google_compute_network.vpc_network.name
      }
    }
  }
}