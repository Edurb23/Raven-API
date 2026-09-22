# Administration

The Raven administration UI is at `/app/admin`. It requires `ROLE_ADMIN`; new
registrations still receive `ROLE_USER`. `/user/me` returns role names so the UI
can display the Admin navigation entry. Every `/admin/**` endpoint also checks
the administrator role on the server.

Administrators can list, create and edit artists, block/unblock them, upload photos
and choose a main photo. Blocking preserves the artist's data while hiding it from
the catalog and normal detail endpoints and rejecting photo votes. Administrators
can still edit blocked artists. Manual selection takes effect immediately; the
next completed weekly election can change the main photo again.

Uploads accept JPEG, PNG and GIF files up to 5 MB. Photo upload and selection
transactions lock the artist to serialize changes with weekly elections.

Feature flags persist in `tb_raven_feature_flags`:

- `artist_catalog`: public artist catalog, detail and photo-voting reads.
- `artist_photo_voting`: reading and casting photo votes. Already recorded votes
  can still be processed by the weekly election scheduler.
- `artist_photo_uploads`: new photo uploads, including administration uploads.

The administration endpoints remain available when the catalog is disabled. The
normal UI receives HTTP 503 for disabled features. Unknown flag keys cannot be
created through the toggle endpoint.

`GET /admin/logs?page=0&errorsOnly=false` returns at most 50 request activity
records. Records contain a timestamp, optional authenticated user ID, HTTP method,
path, status and duration. Request bodies, query strings, authorization headers,
passwords and tokens are excluded. These are request activity logs recorded since
this version started, not historical console output or stack traces. The logs
endpoint itself is not recorded. Logs currently have no automatic retention cleanup.

Liquibase migration `009-admin-console.sql` adds blocked status, feature flags and
the log table. Account credentials and role assignments are not seeded by this
migration; never commit administrator passwords to the repository.
