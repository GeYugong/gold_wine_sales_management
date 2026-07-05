# CLAUDE.md

## Language

Always respond to the user in Simplified Chinese.

## Project Constraints

- This project is a Java advanced programming course assignment.
- The application must be a console program.
- The application must use a database for data persistence.
- Use a layered architecture.
- Prefer Maven + JDBC + SQLite.
- Do not use JavaWeb, Spring Boot, Swing, JavaFX, Android, WeChat mini programs, or similar application forms.

## Development Policy

- Build runnable functionality first, then add comments in a unified pass.
- Keep the implementation simple and suitable for a course assignment and defense demo.
- Do not introduce extra frameworks, frontend interfaces, or complex deployment solutions unless explicitly requested.
- Keep changes focused on the current request and avoid unrelated expansion.

## Collaboration Policy

- The `main` branch must always stay runnable.
- Do not commit directly to `main` unless the team explicitly agrees.
- Develop each feature or fix on a separate branch.
- Use short and descriptive branch names, such as `feat/wine-management`, `feat/order-checkout`, `fix/database-init`, or `docs/readme`.
- Keep each Pull Request focused on one feature, bug fix, or documentation change.
- Do not mix unrelated changes in the same Pull Request.
- Before opening a Pull Request, run the project locally and confirm that the main console flow works.
- At least one teammate should review a Pull Request before it is merged.
- Pull Request descriptions should explain what changed, how it was tested, and whether database schema or seed data changed.
- Database schema changes must be clearly mentioned in the Pull Request description.
- The Pull Request author should resolve merge conflicts locally before requesting another review.

## Commit Message Format

```text
<type>(<scope>): <short description>

# Optional: a longer description explaining why the change was made.
```

Types:

| Type | When to use |
|------|-------------|
| `feat` | New feature |
| `fix` | Bug fix |
| `refactor` | Refactor without behavior change |
| `chore` | Configuration, dependencies, tooling |
| `docs` | Documentation |
| `release` | Version release |
