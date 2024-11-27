terraform {
  backend "gcs" {
    prefix = "terraform/state/infra"
    bucket = "${var.gcp_project_id}-tfstate"
  }

  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 6.11.1"
    }
  }

  required_version = ">= 1.8.5" # this matches the opentofu version in .opentofu-version
}

provider "google" {
  project = var.gcp_project_id
  region  = var.region
}
