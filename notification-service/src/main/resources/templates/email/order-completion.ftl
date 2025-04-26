<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Order Confirmation</title>
    <style>
        /* Reset & basic styles */
        body { margin: 0; padding: 0; background-color: #f6f6f6; font-family: Arial, sans-serif; }
        a { color: #1a82e2; text-decoration: none; }
        /* Container */
        .container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            background-color: #ffffff;
        }
        h1 { font-size: 28px; margin-bottom: 10px; }
        h2 { font-size: 22px; margin: 20px 0 10px; }
        p { font-size: 16px; line-height: 1.5; margin: 0 0 15px; }
        /* Order details table */
        .order-details { width: 100%; margin-bottom: 20px; }
        .order-details td { padding: 6px 0; vertical-align: top; }
        /* Items table */
        .items-table {
            width: 100%;
            border-collapse: collapse;
        }
        .items-table th,
        .items-table td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
            font-size: 16px;
        }
        .items-table th { background-color: #f4f4f4; }
        /* Responsive tweaks */
        @media only screen and (max-width: 480px) {
            .container { padding: 10px; }
            h1 { font-size: 24px; }
            h2 { font-size: 20px; }
            p, .items-table th, .items-table td { font-size: 14px; }
            .items-table th, .items-table td { padding: 6px; }
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Thank you for your order, ${buyerName}!</h1>

    <p>Your payment has been <strong>successfully processed</strong>. Below are your order details:</p>

    <table class="order-details">
        <tr>
            <td><strong>Transaction ID:</strong></td>
            <td>${txnId}</td>
        </tr>
        <tr>
            <td><strong>Payment Method:</strong></td>
            <td>${paymentMethod}</td>
        </tr>
        <tr>
            <td><strong>Total Amount:</strong></td>
            <td>${totalAmount?string["#,##0.00"]}</td>
        </tr>
    </table>

    <h2>Items Ordered</h2>
    <table class="items-table">
        <tr>
            <th>Product</th>
            <th>Quantity</th>
            <th>Price</th>
        </tr>
        <#list items as item>
            <tr>
                <td>${item.productName}</td>
                <td>${item.quantity}</td>
                <td>${item.price?string["#,##0.00"]}</td>
            </tr>
        </#list>
    </table>

    <p>If you have any questions, just reply to this email or reach out to our support team at <a href="mailto:support@example.com">support@example.com</a>.</p>

    <p>Best regards,<br/>
        The YourCompany Team</p>
</div>
</body>
</html>
