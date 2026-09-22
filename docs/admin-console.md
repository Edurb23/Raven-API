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

`DELETE /admin/artists/{id}/images/{imageId}` removes a gallery photo (admin only).
The image must belong to that artist. Deletion locks the artist, removes associated
votes via the existing foreign keys and clears historical winner references while
preserving election records. If the main photo is removed, the oldest remaining
photo becomes the main photo; with no photos left, the UI shows its empty state.
Custom backgrounds are independent and stay unchanged. Removal remains available
when uploads are disabled.

Artist backgrounds are separate from gallery photos and weekly elections.
`POST /admin/artists/{id}/banner` uploads multipart `file` with the same format,
size and feature flag checks as photo uploads. `DELETE /admin/artists/{id}/banner`
restores the main-photo fallback. Both endpoints require `ROLE_ADMIN`.
The detail response includes nullable `bannerImage` (original Base64 bytes);
list responses omit it. Migration `010-artist-banner.sql` adds the column.
The admin preview shows the source dimensions and warns below 1920 x 720 px.
For this layout, use a sharp landscape original around 2560 x 960 px (8:3),
with the subject near the center to accommodate responsive cropping.

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
