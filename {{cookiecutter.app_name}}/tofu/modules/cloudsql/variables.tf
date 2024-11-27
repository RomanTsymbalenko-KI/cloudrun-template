variable "region" {
  description = "The GCP region to deploy resources"
  type        = string
  default     = "europe-west1"
}

variable "db_instance_name" {
  description = "The name of the DB server instance."
  type        = string
}

variable "db_name" {
  description = "The name of the DB within the DB server instance."
  type        = string
}

variable "db_version" {
  description = "The MySQL, PostgreSQL or SQL Server (beta) version to use."
  type        = string
  default     = "POSTGRES_15"
}

variable "db_tier" {
  description = "The machine type to use."
  type        = string
  default     = "db-custom-2-7680"
}

variable "application_user_service_account_email" {
  description = "The Service Account email for the Cloud IAM DB User for the application."
  type        = string
}

variable "vpc_network_id" {
  description = "The ID of the VPC Network for the Cloud SQL Instance Private IP Network configuration."
  type        = string
}

variable "vpc_network_name" {
  description = "The name of the VPC Network for the Cloud SQL Instance Private IP Network configuration."
  type        = string
}
