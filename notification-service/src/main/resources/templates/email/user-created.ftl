<!DOCTYPE html>
<html lang="${lang?default("en")}">
<head>
    <meta charset="UTF-8">
    <title>${subject?default("Welcome to bTailor!")}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        :root {
            color-scheme: light dark;
        }

        body {
            margin: 0;
            padding: 0;
            font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
            background-color: #f6f6f6;
            color: #333333;
        }

        .container {
            max-width: 600px;
            margin: 30px auto;
            background-color: #ffffff;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            padding: 30px 20px;
        }

        .header {
            text-align: center;
            padding-bottom: 20px;
        }

        .logo {
            max-width: 120px;
            margin-bottom: 15px;
        }

        .header h1 {
            color: #4CAF50;
            margin: 0;
        }

        .content {
            font-size: 16px;
            line-height: 1.6;
        }

        .button {
            display: inline-block;
            margin-top: 20px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            padding: 12px 20px;
            border-radius: 6px;
            font-weight: bold;
        }

        .footer {
            margin-top: 30px;
            font-size: 13px;
            text-align: center;
            color: #888;
        }

        /* 🌙 Dark mode */
        @media (prefers-color-scheme: dark) {
            body {
                background-color: #1e1e1e;
                color: #dddddd;
            }

            .container {
                background-color: #2b2b2b;
                color: #dddddd;
                box-shadow: none;
            }

            .button {
                background-color: #5ecf64;
            }

            .footer {
                color: #999;
            }
        }

        @media only screen and (max-width: 600px) {
            .container {
                margin: 15px;
                padding: 20px 15px;
            }
        }
    </style>
</head>
<body>

<div class="container">
    <div class="header">
        <img class="logo" src="${logoUrl}" alt="bTailor Logo">
        <h1>${heading?default("Welcome, " + name + "!")}</h1>
    </div>

    <div class="content">
        <p>${intro?default("We're thrilled to have you on board. Your account has been successfully created.")}</p>

        <ul>
            <li>${feature1?default("Track your orders in real-time")}</li>
            <li>${feature2?default("Get tailored notifications")}</li>
            <li>${feature3?default("Customize your experience with bTailor")}</li>
        </ul>

        <p>${ctaText?default("Click below to log in and get started:")}</p>

        <p><a class="button" href="${loginUrl}">${loginButtonText?default("Go to Dashboard")}</a></p>
    </div>

    <div class="footer">
        <p>${disclaimer?default("If you didn’t sign up for this account, please ignore this email or contact support.")}</p>
        <p>&copy; ${year?default("2025")} bTailor</p>
    </div>
</div>

</body>
</html>
