# GitHub Workflows and Actions

## Actions

- [`docker`](./actions/docker/action.yaml): Builds and conditionally pushes a Docker image to the `ki-artifact-registries-cf08` Docker registry.

## Workflows

- [`main.yaml`](./workflows/main.yaml): Entry point to the CI/CD process.
- [`tofu-app.yaml`](./workflows/tofu-app.yaml): Reusable Workflow to plan and apply `/tofu/app` OpenTofu, for deploying the application to GCP Cloud Run.
- [`tofu-infra.yaml`](./workflows/tofu-infra.yaml): Reusable Workflow to plan and apply `/tofu/infra` OpenTofu, for provisioning other cloud infrastructure resources.

## Overview

The following steps occur in sequence:

1. **Test and Build**: The application is tested and built. Application-specific test and build steps should go here.
2. **Docker**: A Docker image is built using the application artefact.
    - If and only if the pipeline is running for a commit on `main`, the Docker image is pushed to the registry, tagged with the Git SHA, Workflow run number and Workflow run attempt.
3. **Tofu**: Always runs for `main` branch. For other branches, runs only if there are changes to files in `/tofu`. For each environment in sequence:
    - **Tofu Infra**: Executes a Tofu `init`, `fmt`, and `plan` in the `/tofu/infra` directory. Executes `apply` only on the `main` branch, which propagates any cloud infrastructure changes to the environment.
    - **Tofu App**: Executes a Tofu `init`, `fmt`, and `plan` in the `/tofu/app` directory. Executes `apply` only on the `main` branch, which deploys a new version of the application to GCP Cloud Run to the environment.

For the Tofu Workflows:

- The [GitHub Environment](https://docs.github.com/en/actions/managing-workflow-runs-and-deployments/managing-deployments/managing-environments-for-deployment) is specified for this workflow run so that the Tofu commands authenticate and execute against the corresponding GCP Project for the environment, by using the Service Account and GCP Workload Identity Provider variables defined in that GitHub Environment. It also controls which Tofu variables `.tfvars` file to use as inputs.
- The `init` command specifies the environment-specific Tofu variables `.tfvars` file so that the GCP Bucket name that stores the remote Tofu state can be resolved from the GCP Project ID.
- The `plan` command specifies both the environment-specific Tofu variables `.tfvars` file and an inline variable for the Docker image tag.
- The Tofu state lock is acquired for `init` and `plan` commands for Workflows running for the `main` branch, but not for other branches. This is to avoid Workflow failures due to an already acquired lock when multiple Workflows are running concurrently for other branches.
