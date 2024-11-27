output "vpc_network_name" {
  value = google_compute_network.vpc_network.name
}

output "cloudrun_service_identity_email" {
  value = google_service_account.cloudrun_service_identity.email
}

output "metrics_bucket_config_name" {
  value = google_storage_bucket.metrics_config_bucket.name
}
