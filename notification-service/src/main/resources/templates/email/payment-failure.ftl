<#-- payment-failure-mobile.ftl -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Payment Failed</title>
    <style>
        body { margin: 0; padding: 0; background-color: #f6f6f6; font-family: Arial, sans-serif; }
        .container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            background-color: #ffffff;
        }
        h1 { font-size: 24px; margin-bottom: 10px; color: #d32f2f; }
        p { font-size: 16px; line-height: 1.5; margin: 0 0 15px; }
        .details-table, .items-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        .details-table td {
            padding: 6px 0;
            vertical-align: top;
            font-size: 16px;
        }
        .items-table th,
        .items-table td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
            font-size: 16px;
        }
        .items-table th { background-color: #f4f4f4; }
        @media only screen and (max-width: 480px) {
            .container { padding: 10px; }
            h1 { font-size: 20px; }
            p, .details-table td, .items-table th, .items-table td { font-size: 14px; }
            .items-table th, .items-table td { padding: 6px; }
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Payment Failed</h1>

    <p>Hi ${buyerName}, unfortunately your payment could not be processed.</p>

    <table class="details-table">
        <tr>
            <td><strong>Order ID:</strong></td>
            <td>${id}</td>
        </tr>
        <tr>
            <td><strong>Payment Method:</strong></td>
            <td>${paymentMethod}</td>
        </tr>
        <tr>
            <td><strong>Total Amount:</strong></td>
            <td>${totalAmount?string["#,##0.00"]}</td>
        </tr>
        <tr>
            <td><strong>Reason:</strong></td>
            <td>${failureReason}</td>
        </tr>
    </table>

    <h2>Items in Your Order</h2>
    <table class="items-table">
        <tr>
            <th>Product</th>
            <th>Qty</th>
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

    <p>Please try again or use a different payment method. If you continue to experience issues, contact our support team at <a href="mailto:support@example.com">support@example.com</a>.</p>

    <p>Thank you for your patience,<br/>The YourCompany Team</p>
</div>
</body>
</html>
