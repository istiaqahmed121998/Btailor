<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Your Password Reset Code</title>
    <style>
        /* Basic styles for email clients */
        body {
            margin: 0;
            padding: 0;
            background-color: #f6f9fc;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
        }
        .container {
            max-width: 600px;
            margin: 40px auto;
            padding: 20px;
            background-color: #ffffff;
            border: 1px solid #e6ebf1;
            border-radius: 8px;
        }
        h1 {
            font-size: 24px;
            color: #333333;
            margin-bottom: 20px;
        }
        p {
            font-size: 16px;
            line-height: 1.6;
            color: #555555;
            margin: 0 0 20px;
        }
        .otp-code {
            display: inline-block;
            width: 100%;
            padding: 20px 0;
            margin: 20px 0;
            font-size: 32px;
            font-weight: bold;
            letter-spacing: 4px;
            color: #1a82e2;
            background-color: #e8f0fe;
            text-align: center;
            border-radius: 6px;
        }
        .footer {
            font-size: 14px;
            color: #888888;
            margin-top: 30px;
            text-align: center;
        }
        a {
            color: #1a82e2;
            text-decoration: none;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Password Reset Request</h1>

    <p>We received a request to reset the password for your account. Please use the one-time password (OTP) below to proceed.</p>

    <div class="otp-code">
        ${otpCode}
    </div>

    <p>This code is valid for <strong>10 minutes</strong>. For your security, please do not share this code with anyone.</p>

    <p>If you did not request a password reset, please ignore this email or contact our support team if you have any concerns.</p>

    <p>Best regards,<br/>
        The YourCompany Team</p>
</div>
<div class="footer">
    <p>If you have any questions, contact us at <a href="mailto:support@example.com">support@example.com</a>.</p>
</div>
</body>
</html>