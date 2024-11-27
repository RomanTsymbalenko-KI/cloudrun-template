# cloudrun-kotlin

Template for deploying a Dockerised application to [GCP Cloud Run](https://cloud.google.com/run) via OpenTofu and GitHub Workflows.

## Structure

- `/.github/` - GitHub Workflows, see [.github/WORKFLOWS.md](/.github/WORKFLOWS.md).
- `/app/` - Application code, see [app/README.md](/app/README.md).
- `/tofu/` - OpenTofu code, see [tofu/TOFU.md](/tofu/TOFU.md).

## Environments

Environments correspond to those specified in [ki-landing-zone-projects](https://github.com/Ki-Insurance/ki-landing-zone-projects) for your repository. This populates GitHub Environments, which contain relevant Environment Variables used for authenticating to, and making changes in, the corresponding GCP Projects, that are used for cloud infrastructre provisioning and application deployments in the GitHub Workflows.

## Initial Project Deployment

1. Create your GitHub Repository from this template.
2. Add `ki-cicd` as an admin to your GitHub Repository.
3. Raise PRs in the [ki-landing-zone-projects](https://github.com/Ki-Insurance/ki-landing-zone-projects) repo to set up new GCP Projects with enabled GCP APIs, and GCP Cloud Storage Buckets to store the remote OpenTofu state. `ki-landing-zone-projects` struggles to handle multiple GCP APIs being enabled at once, so multiple PRs must be merged in succession:
    - Create a PR based on this [one](https://github.com/Ki-Insurance/ki-landing-zone-projects/pull/440/files), to create the projects/buckets and enable a limited initial set of APIs. Replace your repo and project names accordingly, and remove the `sqladmin.googleapis.com` API if you do not need a Cloud SQL DB.
    - Merge this first PR.
    - Once this has successfully applied, create a second PR based on this [one](https://github.com/Ki-Insurance/ki-landing-zone-projects/pull/441/files) to enable APIs related to observability and artifact registry access.
    - Merge this second PR.
4. Grant the GCP Cloud Run Service Agent Service Accounts read access to the Ki Docker Registry, for each GCP Project you created. The Service Account emails are in the format `service-<GCP Project Number>@serverless-robot-prod.iam.gserviceaccount.com`. See example [PR](https://github.com/Ki-Insurance/ki-artifact-registries/pull/103/files). A GCP Project Number can be retrieved from [console.cloud.google.com](https://console.cloud.google.com/) after you select a GCP Project.
5. Run the `bootstrap.sh` script in the root and follow the instructions.
6. Review the changes to ensure that the values in the following files to correspond to your GCP Projects and application:
    - [`/tofu/environments/Dev/Dev.tfvars`](/tofu/environments/Dev/Dev.tfvars)
    - [`/tofu/environments/Live/Live.tfvars`](/tofu/environments/Live/Live.tfvars)
7. Create a PR with the changes. When the PR is merged, the workflows will provision all cloud infrastructure and deploy your application for the first time.
