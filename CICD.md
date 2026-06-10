# CI/CD Pipeline — TaskList (Java + Maven)

Pipeline GitHub Actions untuk project ini, dari **integration** sampai **deployment**.

## Ringkasan

| Workflow | File | Trigger | Fungsi |
|----------|------|---------|--------|
| **CI** | `.github/workflows/ci.yml` | push / PR ke `main`/`master` | Build, unit test (Surefire), integration test (Failsafe), coverage (JaCoCo), upload JAR |
| **CD - Release** | `.github/workflows/cd-release.yml` | push tag `v*` | Build + verify, lalu publish GitHub Release berisi JAR |
| **Dependency Submission** | `.github/workflows/dependency-submission.yml` | push ke `main`/`master` | Submit dependency graph Maven ke GitHub (Dependabot) |

## Tahapan pipeline

```
push / PR ──► CI ──────────────► compile → unit test → package → integration test → verify → coverage → artifact
push tag v* ─► CD - Release ────► clean verify → buat GitHub Release + lampirkan JAR
push main ──► Dependency Submit ► submit dependency tree Maven ke Dependency Graph
```

### 1. Integration (CI)
`mvn verify` menjalankan seluruh rantai Maven:
- **compile** — kompilasi `src/main/java`
- **test** — unit test `*Test.java` lewat **maven-surefire-plugin**
- **package** — buat `target/tasklist.jar`
- **integration-test** — test `*IT.java` lewat **maven-failsafe-plugin**
- **verify** — laporan coverage **JaCoCo** (`target/site/jacoco/`)

### 2. Deployment (CD)
Buat rilis dengan menge-push tag versi:

```bash
git tag v1.0.0
git push origin v1.0.0
```

Workflow `cd-release.yml` akan build, jalankan `verify` (test harus lulus), lalu membuat **GitHub Release** dan melampirkan `tasklist-v1.0.0.jar`.

## Tentang error sebelumnya

Error:

```
Could not generate a snapshot of the dependencies;
Cannot read properties of undefined (reading 'forEach')
```

Penyebabnya: action dependency-submission mencoba membaca dependency tree, tapi `pom.xml` lama **tidak punya blok `<dependencies>` sama sekali**, sehingga snapshot-nya `undefined` dan `.forEach()` gagal.

Perbaikan:
1. `pom.xml` sekarang punya blok `<dependencies>` (JUnit 5) yang valid.
2. Dependency submission memakai action Maven resmi `advanced-security/maven-dependency-submission-action@v4`, yang me-resolve dependency lewat Maven (`mvn`) — bukan parsing manual.

## Menjalankan secara lokal

```bash
mvn clean verify        # build + semua test + coverage
mvn -Dtest=TaskTest test   # satu test class saja
mvn exec:java           # jalankan aplikasi
java -jar target/tasklist.jar
```

## Struktur test

```
src/test/java/
├── task/
│   ├── TaskManagerTest.java   # add / edit / delete / persistensi
│   └── TaskTest.java          # Personal/Work task, getter, setter
├── user/
│   ├── UserManagerTest.java   # register / login / ganti password / hapus
│   └── AccountTest.java       # autentikasi Account
└── tasklist/
    └── TaskListFlowIT.java    # integration test end-to-end
```
