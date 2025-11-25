
Progress update: I created the review plan and will now produce focused recommendations across the repo (quick wins, code/design, tests, infra, and docs). Below are prioritized, actionable directions — I will not write exact code, only guidance.

**Quick Wins**
- **`package.json` fix**: Remove `node` from `dependencies` (that's a runtime, not a package). Move `nodemon` to `devDependencies`. Add an `engines` field to indicate supported Node versions.
- **.dockerignore**: Add a .dockerignore (e.g., target, `node_modules/`, .git) to speed image builds and reduce context.
- **Health & graceful shutdown**: Ensure both the Java app and Node server respond to SIGTERM for graceful shutdown. Avoid backgrounding processes in `CMD` — use a process supervisor or separate containers.
- **Logging to stdout/stderr**: Ensure both components log to stdout/stderr (no file-only logs) so Docker/Kubernetes can collect them.
- **Basic input validation**: Add size limits, type checks, and filename sanitization on any file upload endpoints.

**Docker / Containerization**
- **Single responsibility**: Prefer separating the Java processor and Node server into two containers (one per process). Running two long-lived processes in a single container makes lifecycle and scaling harder.
- **Use lightweight base images**: Align JDK versions across build and runtime stages (pom uses Java 17; final image uses Temurin 23). Pick one JDK (or document why they differ).
- **Entrypoint syntax and PID1**: Replace `sh -c "java -jar app.jar & cd /server && node server.js"` with a more robust approach: either use an init process (tini) or run separate containers and orchestrate them. If keeping a single image, use `exec` form with a small supervisor so signals propagate.
- **Set non-root user**: Run the apps unprivileged inside the container.
- **Expose / mount volumes**: Document or configure `VIDEO_DIRECTORY` and `RESULT_DIRECTORY` as volumes; validate them at startup.

**Build & Packaging**
- **Maven assembly vs shade**: Consider `maven-shade-plugin` for fat JARs (it handles relocations better). Confirm the produced JAR name matches the Dockerfile copy pattern.
- **Main class consistency**: Ensure the `mainClass` used by `exec-maven-plugin` and the manifest (`maven-assembly-plugin`) point to the intended entrypoint.
- **Pin build environments**: Keep consistent JDK versions between CI, local dev, and Docker stages.

**Java (processor) — code quality & reliability**
- **Resource management**: Always close/stop `FrameGrabber`/`FrameRecorder`/native resources in try-with-resources or finally blocks. Use timeouts to avoid hangs.
- **Logging framework**: Use SLF4J + Logback (or similar) rather than System.out/err. Include structured logs and levels (INFO/WARN/ERROR).
- **Error handling**: Surface meaningful exceptions up to the service layer; add retries for transient errors and fail-fast for unrecoverable ones. Avoid swallowing exceptions.
- **Unit testability**: Decouple FFmpeg/javacv usage behind an interface so you can mock video IO in unit tests. Provide small adapters and integration tests that exercise native parts separately.
- **Concurrency & memory**: Avoid loading whole frames into memory. If processing multiple videos concurrently, use a bounded ExecutorService and monitor memory/cpu. Consider back-pressure or a queue when CPU-bound.
- **Algorithm correctness**: For algorithms like centroid or group finding (e.g., DFS), ensure edge cases are tested: empty frames, fully-black/white frames, single-pixel groups, overlapping groups, boundary pixels.

**Node (server) — robustness & security**
- **Validate env vars**: Use a small schema or library to validate required env vars (ports, directories) on startup and fail with helpful messages if missing.
- **Express best practices**: Add centralized error handler middleware, input validation (e.g., using `celebrate`/`joi`), rate limiting, and CORS configuration.
- **Security headers**: Use `helmet` and other middleware to set recommended HTTP headers.
- **File upload safety**: Limit upload sizes, check MIME types, and write uploads to a temporary directory with controlled permissions and cleanup.
- **Logging & metrics**: Use `pino` or `winston` with log-to-stdout and structured JSON; add basic metrics (request counts, latencies).
- **Package hygiene**: Run `npm audit` and consider pinning critical packages. Remove the `node` dependency (already noted).

**Testing**
- **Unit tests**: Improve unit coverage in both Java and Node. For Node use `supertest` (or equivalent) to test routes; for Java keep using JUnit 5 and add tests for core processing classes.
- **Integration tests**: Add integration tests with a small sample video (or synthetic frames). Keep integration tests in a separate profile so they can run in CI optionally (they'll be slower and require native libs).
- **Mocking native parts**: For Java, abstract the FFmpeg/javacv parts and mock them in fast unit tests to simulate frame streams.
- **CI tests**: Add GitHub Actions to run `mvn -DskipTests=false test` and Node tests; optionally build Docker images and run smoke tests against them.

**Performance & architecture**
- **Process videos asynchronously**: Don't block HTTP requests for long-running processing. Accept uploads, enqueue processing jobs, and return job IDs with status endpoints. Use a worker pool for processing (separate process).
- **Streaming I/O**: Process video frames as streams to keep memory use low.
- **Scaling**: If workload grows, separate API (Node) from processing workers (Java) and use a message queue (RabbitMQ, Redis Streams).
- **Native ffmpeg**: Evaluate whether calling the native `ffmpeg` binary (via a small wrapper) could be simpler/more performant than heavy Java natives — or run ffmpeg in a sidecar container.

**Observability & monitoring**
- **Metrics**: Add Prometheus metrics (latency, job counts, success/failures) to the Java and Node apps.
- **Tracing**: Consider lightweight tracing (OpenTelemetry) for cross-service visibility if you split components.
- **Health checks**: Add `/health` and `/ready` endpoints for both services.

**Security**
- **Secrets**: Never commit secrets or credentials; use environment variables and document how to supply them.
- **Dependency scanning**: Set up automated dependency scanning (GitHub Dependabot or similar).
- **Input sanitization**: Sanitize filenames and user inputs to avoid path traversal.

**Documentation**
- **README**: Expand README.md with:
  - How to build and run locally (Java and Node),
  - How to run with Docker (or the recommendation to use separate containers),
  - Environment variables and defaults,
  - Where test videos live and how to run tests.
- **API docs**: Add OpenAPI/Swagger for HTTP endpoints.
- **Developer guide**: Add short notes describing architecture decisions (why Java for processing, how Node interacts), and how to add new tests or dependencies.

**CI/CD / repo hygiene**
- **Add GitHub Actions**:
  - Run `mvn -DskipTests=false test` (or configure profiles) and Node tests.
  - Linting steps for Java (SpotBugs/Checkstyle) and Node (ESLint).
  - Optionally build Docker images and run a smoke test.
- **.gitignore**: Ensure target, `node_modules/`, and other build artifacts are ignored.
- **Versioning & releases**: Add release workflow or tags to control version bumps.

**Prioritized Roadmap (recommended order)**
- 1) Quick fixes: `package.json`, .dockerignore, .gitignore, move nodemon to devDeps.
- 2) Docker and runtime: split processes or at least make the entrypoint robust and add graceful shutdown.
- 3) Logging, env validation, and input validation (Node + Java).
- 4) Add unit tests and some lightweight integration tests for both components.
- 5) Set up CI: run tests and lint on PRs.
- 6) Performance & architecture: enqueue long jobs, bounded worker pool, streaming frames.
- 7) Observability & security hardening.

What I can do next (pick one)
- Produce a concise PR checklist and example GitHub Actions workflows.
- Create a safe `package.json` fix patch and .dockerignore file.
- Draft a README section describing local dev and Docker runs.
- Sketch an architecture doc for separating API and worker.
