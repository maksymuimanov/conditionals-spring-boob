# Security Policy

## Reporting a vulnerability

If you believe you have found a security vulnerability in `conditionals-spring-boot`, please report it privately.

- Do not open a public GitHub issue.
- Do not disclose the vulnerability publicly until a fix is released.

### Contact

Report security issues by email to:

- uimanovmaks@gmail.com

### What to include

To help triage efficiently, include:

- A clear description of the issue and the potential impact.
- A minimal reproduction (sample configuration, code snippet, and steps).
- Affected versions.
- Any relevant logs or stack traces.
- Suggested remediation, if you have one.

If the issue depends on a specific Spring Boot or Java runtime behavior, include:

- Java version
- Spring Boot version
- Build tool and version (Maven/Gradle)

## Supported versions

Security fixes are provided for the latest released version.

If you are using an older version, you may be asked to upgrade to the latest version to verify whether the issue is still present.

## Coordinated disclosure

This project follows a coordinated disclosure process:

- You report the issue privately.
- The maintainer confirms the report and evaluates impact.
- A fix is developed and released.
- Public communication may follow after a release is available.

## Response expectations

The maintainer will make a reasonable effort to:

- Acknowledge receipt of a report.
- Provide an initial assessment or request additional information.

Response and release timelines depend on severity, reproducibility, and maintainer availability.

## Security considerations

This library is a set of Spring Boot condition annotations and does not provide network services. Potential security issues are typically related to:

- Unexpected behavior triggered by configuration inputs.
- Denial-of-service risks due to expensive matching operations (for example, complex regular expressions with `@ConditionalOnStringProperty(matchType = MATCHES)`).
- Error handling that could expose sensitive information through logs in certain environments.
