# TOFU

## `/tofu`

Contains OpenTofu code declaring cloud infrastructure resources. This template provides:

- A dedicated Service Account for the Cloud Run Service.
- A VPC network for the Direct VPC Egress for Cloud Run (can be used for connectivity to other cloud components, e.g. Cloud SQL).
- An owner role IAM binding for the GCP Projects.
- A GCP Cloud Run Service that runs your application.

## `/tofu/environments`

Contains directories with configuration for each environment. These should correspond to your [GitHub Environments](https://docs.github.com/en/actions/managing-workflow-runs-and-deployments/managing-deployments/managing-environments-for-deployment), that were specified during GCP Project creation in [ki-landing-zone-projects](https://github.com/Ki-Insurance/ki-landing-zone-projects). The naming of the directories should also match the environment name, e.g. `Dev, Live`.

Each environment directory contains:

- A `<env>.tfvars` file, which contains environment specific Tofu inputs.

If you wish to add a new environment:

- Create a new directory matching the new environment name,
- Populate it with the two files as above and update the values corresponding to the new environment and GCP Project,
- Add the environment name as a new element to the `jobs.tofu.strategy.matrix.env` array in [../.github/workflows/main.yaml](../.github/workflows/main.yaml), so that the GitHub Workflows plans/applies OpenTofu against the new environment.

## Running commands locally

### Setup

1. Install `tenv`:

    ```bash
    brew install tenv
    ```

2. Install amd64 distribution of Tofu with the version specified in `.opentofu-version`, so that this matches GitHub Workflows runners:

    ```bash
    TENV_ARCH=amd64 tenv opentofu install
    ```

### Commands

Tofu commands can be run locally for faster feedback on Tofu changes. See the Tofu commands in [`app-tofu.yaml`](../.github/workflows/app-Tofu.yaml) and [`infra-tofu.yaml`](../.github/workflows/infra-tofu.yaml) Workflows for reference.

If you haven't already done so, authenticate your Google Cloud SDK installation and set up the Application Default Credentials and set the default GCP Project ID:

```bash
gcloud auth application-default login
gcloud config set project <GCP Project ID>
```

The following commands can also be run for the `Live` Environment by changing the path and name for the `.tfvars` file.

#### From `/tofu` directory

The value of `cloud_run_container_image` is not important for local plans, but it is a required Tofu input.

```bash
tofu init -var-file=./environments/Dev/Dev.tfvars -lock=false
tofu plan -var-file=./environments/Dev/Dev.tfvars -var="cloud_run_container_image=foobar" -lock=false 
```
