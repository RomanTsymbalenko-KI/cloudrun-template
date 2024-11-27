gcp_project_id         = "{{cookiecutter.project_id_dev}}"
gcp_project_iam_owners = ["{{cookiecutter.iam_owners_dev}}"]

# Cloud Run specific variables
region                 = "europe-west2"
cloud_run_service_name = "{{cookiecutter.app_name}}"
state_bucket           = "STATE_BUCKET_DEV"
monitoring_project_id  = "nonprod-monitor-5d0f"
