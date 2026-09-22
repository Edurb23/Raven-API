# Weekly artist photos

Each authenticated user has one vote per artist and calendar week. Sending another
vote replaces the previous choice. Weeks run from Monday 00:00 until the following
Monday 00:00 in `America/Sao_Paulo`; the API determines the week and the voter.

- `GET /artist/{artistId}/images/votes` returns the week, closing instant, timezone,
  authenticated user's choice, image vote counts and current selected image flags.
- `PUT /artist/{artistId}/images/{imageId}/vote` saves or replaces a vote and returns
  the updated voting snapshot. No user ID or week is accepted from the client.
- The existing manual `PUT /artist/{artistId}/images/{imageId}/select` is admin-only.

The scheduler checks for completed weeks every minute, beginning ten seconds after
startup. It processes missed weeks in chronological order after downtime. The
winner has the largest vote count. Ties prefer the currently selected image, then
the oldest uploaded image, then the image UUID. Weeks without votes retain the
current photo. The winning `selected` flag is shared by the catalog and artist page.

Votes and completed election records persist in the database. An artist row lock
serializes voting and election transactions across instances. Unique keys prevent
duplicate user/week votes and repeated election results.

Liquibase migration `008-weekly-artist-image-votes.sql` creates both tables on API
startup. Existing votes/photos are not seeded or changed by the migration. Keep
the API running for automatic election processing; catch-up runs on the next start.

Configuration:

- `raven.image-voting.scheduler.enabled` defaults to `true`; set it to `false` for
  application tests that must not run scheduled elections.
- `raven.image-voting.timezone` defaults to `America/Sao_Paulo`. Keep it stable once
  voting starts; the UI uses the timezone returned by the API for deadlines.

The dedicated voting tests use an isolated H2 database in MySQL mode, execute the
actual migration, and cover replacement, concurrent votes/elections, foreign-image
rejection, weekly boundaries, ties, new-week counts and missed-week recovery.
