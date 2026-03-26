# mosterdenmeer

## Simple setup (JVM settings only)

The contact form reads SMTP settings from GlassFish JVM options:

- `SMTP_HOST`
- `SMTP_PORT` (optional, default `587`)
- `SMTP_USER`
- `SMTP_PASSWORD`
- `MAIL_TO`

For captcha protection with Cloudflare Turnstile, also set:

- `TURNSTILE_SITE_KEY`
- `TURNSTILE_SECRET`

Add these in the GlassFish Admin Console:

`Configurations` -> `server-config` -> `JVM Settings` -> `JVM Options`

Example values:

```text
-DSMTP_HOST=smtp.your-provider.com
-DSMTP_PORT=587
-DSMTP_USER=info@mosterdenmeer.nl
-DSMTP_PASSWORD=your-real-password
-DMAIL_TO=info@mosterdenmeer.nl
-DTURNSTILE_SITE_KEY=0x4AAAA...
-DTURNSTILE_SECRET=0x4AAAA...
```

Restart GlassFish after saving JVM options.

## Deploy on Windows

```powershell
Set-Location "C:\Users\phill\Documents\GitHub\mosterdenmeer\mosterdenmeer"
.\mvnw.cmd package cargo:redeploy
```

Open:

```text
http://localhost:8080/mosterdenmeer-1.0-SNAPSHOT/
```

## Deploy on CentOS

```powershell
Set-Location "C:\Users\phill\Documents\GitHub\mosterdenmeer\mosterdenmeer"
.\mvnw.cmd -Dglassfish.home=/home/glassfish/glassfish7-25 `
		   -Dglassfish.domain.home=/home/glassfish/glassfish7-25/glassfish/domains/domain1 `
		   package cargo:redeploy
```

If Cargo is not used for remote deployment, deploy the WAR manually on the server:

```bash
/home/glassfish/glassfish7-25/glassfish/bin/asadmin deploy --force=true --contextroot mosterdenmeer-1.0-SNAPSHOT /path/to/mosterdenmeer-1.0-SNAPSHOT.war
```

## Build and test

```powershell
.\mvnw.cmd -q test
.\mvnw.cmd -q package
```


