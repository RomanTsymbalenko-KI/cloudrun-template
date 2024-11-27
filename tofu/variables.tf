variable "gcp_project_id" {}
variable "region" {}
variable "cloud_run_service_name" {}
variable "gcp_project_iam_owners" {
  type = list(string)
}
variable "monitoring_project_id" {}
variable "cloud_run_container_image" {}