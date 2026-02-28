# Contributing

Thank you for your interest in contributing to `conditionals-spring-boot`.

This document describes the expected workflow and quality requirements for contributions. By submitting a contribution, you agree that it may be redistributed under the project’s license (MIT).

## Scope

Contributions are welcome in the following areas:

- Bug fixes and correctness improvements.
- Documentation improvements (JavaDoc, README, examples).
- Additional conditions or enhancements to existing conditions, provided the behavior is explicit and testable.
- Test coverage and build tooling improvements.

Changes that introduce new runtime dependencies, dynamic expression evaluation, or behavior that is difficult to reason about are less likely to be accepted.

## Getting started

### Prerequisites

- JDK 17.
- Maven (or the included Maven Wrapper: `mvnw` / `mvnw.cmd`).

### Build and test

Run the test suite:

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

## Reporting issues

- Use the GitHub issue tracker.
- Provide a minimal reproduction when possible.
- Include:
  - Spring Boot version
  - Java version
  - Expected behavior
  - Actual behavior
  - Relevant configuration properties and annotations

If you are proposing a new annotation or a behavioral change, describe the intended semantics precisely (evaluation order, handling of missing properties, conversion behavior, and diagnostic output).

## Development workflow

### Branching

- Create a topic branch from `main`.
- Use a descriptive branch name, for example:
  - `fix/os-condition-message`
  - `docs/readme-usage`
  - `feature/string-match-trim`

### Commit messages

Use clear, imperative commit messages:

- `Fix enum conversion when property is lower-case`
- `Add tests for matchIfMissing behavior`
- `Refine JavaDoc for ConditionUtils`

Squash commits if the PR history is noisy.

### Pull requests

A pull request should include:

- A concise description of the change and its motivation.
- The behavioral impact (including compatibility considerations).
- Tests that cover the change.
- Documentation updates if public API behavior changes.

## Coding standards

### General

- Keep changes focused. Prefer small PRs over large, unrelated refactors.
- Avoid introducing unnecessary abstractions.
- Prefer explicit code paths and documented semantics over “smart” behavior.

### API and semantics

This library is intentionally explicit about conditional behavior. When changing or adding a condition, document and preserve:

- Evaluation order.
- Aggregation semantics for repeatable annotations.
- Missing-property behavior (`matchIfMissing`).
- Conversion rules and failure behavior.
- Diagnostics (match/no-match messages) where applicable.

### Nullness

The codebase uses JSpecify nullness annotations (`@NullMarked`, `@Nullable`).

- Do not introduce unchecked null handling.
- If a new public API can accept or return `null`, annotate it explicitly.

### JavaDoc

Public classes, annotations, and methods should have JavaDoc that is:

- Precise and implementation-aligned.
- Explicit about semantics (especially evaluation order).
- Explicit about thread safety and null handling.

Avoid marketing language and avoid vague statements.

## Tests

- Add or update tests for behavioral changes.
- Prefer tests that exercise the condition evaluation via Spring’s `Environment` and `AnnotatedTypeMetadata` where applicable.
- Include edge cases:
  - Missing properties
  - Invalid property values (conversion errors)
  - Regex invalid patterns (for string `MATCHES`)
  - Enum conversion failures
  - NaN handling for float conditions

## Compatibility

- The library targets Spring Boot 3.x and Java 17 (see `pom.xml`).
- Avoid introducing behavior that depends on unspecified ordering or non-deterministic environment state.

## Security and responsible disclosure

If you believe you have found a security issue, do not open a public issue. Instead, contact the maintainer via the email address listed in `pom.xml`.

## License

By contributing, you agree that your contributions will be licensed under the MIT License.
