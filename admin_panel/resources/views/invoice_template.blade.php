<html lang="en">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>{{App\Models\Setting::getSetting('app_name')}}</title>
    <style>
        .clearfix:after {
            content: "";
            display: table;
            clear: both;
        }

        a {
            text-decoration: none;
        }

        body {
            position: relative;
            width: 100%;
            height: auto;
            margin: 0 auto;
            color: #555555;
            background: #FFFFFF;
            font-size: 13px;
            font-family: Verdana, Arial, Helvetica, sans-serif;
        }

        h2 {
            font-weight: normal;
        }

        header {
            padding: 10px 0;
        }

        #logo img {
            height: 50px;
            margin-bottom: 15px;
        }

        #details {
            margin-bottom: 25px;
        }

        #client {
            padding-left: 6px;
            float: left;
        }

        #client .to {
            color: #777777;
        }

        h2.name {
            font-size: 1.2em;
            font-weight: normal;
            margin: 0;
        }

        #invoice h1 {
            color: #0087C3;
            line-height: 2em;
            font-weight: normal;
            margin: 0 0 10px 0;
            font-size: 20px;
        }

        #invoice .date {
            font-size: 1.1em;
            color: #777777;
        }

        table {
            width: 100%;
            border-spacing: 0;
            /* margin-bottom: 20px; */
        }

        table th,
        table td {
            padding: 5px 8px;
            text-align: center;
        }

        table th {
            background: #EEEEEE;
        }

        table th {
            white-space: nowrap;
            font-weight: normal;
        }

        table td {
            text-align: right;
        }

        table td.desc h3,
        table td.qty h3 {
            font-size: 0.9em;
            font-weight: normal;
            margin: 0 0 0 0;
        }

        table .no {
            font-size: 1.2em;
            width: 10%;
            text-align: center;
            border-left: 1px solid #e7e9eb;
        }

        table .desc,
        table .item-summary {
            text-align: left;
        }

        table .unit {
            /* background: #DDDDDD; */
            border: 1px solid #e7e9eb;
        }


        table .total {
            background: #57B223;
            color: #FFFFFF;
        }

        table td.unit,
        table td.qty,
        table td.total {
            font-size: 1.2em;
            text-align: center;
        }

        table td.unit {
            width: 35%;
        }

        table td.desc {
            width: 45%;
        }

        table td.qty {
            width: 5%;
        }

        .status {
            margin-top: 15px;
            padding: 1px 8px 5px;
            font-size: 1.3em;
            width: 80px;
            float: right;
            text-align: center;
            display: inline-block;
        }

        .status.unpaid {
            background-color: #E7505A;
        }

        .status.paid {
            background-color: #26C281;
        }

        .status.cancelled {
            background-color: #95A5A6;
        }

        .status.error {
            background-color: #F4D03F;
        }

        table tr.tax .desc {
            text-align: right;
        }

        table tr.discount .desc {
            text-align: right;
            color: #E43A45;
        }

        table tr.subtotal .desc {
            text-align: right;
        }


        table tfoot td {
            padding: 10px;
            font-size: 1.2em;
            white-space: nowrap;
            border-bottom: 1px solid #e7e9eb;
            font-weight: 700;
        }

        table tfoot tr:first-child td {
            border-top: none;
        }

        table tfoot tr td:first-child {
            /* border: none; */
        }


        #notices {
            padding-left: 6px;
            border-left: 6px solid #0087C3;
        }

        #notices .notice {
            font-size: 1.2em;
        }

        footer {
            color: #777777;
            width: 100%;
            height: 30px;
            position: absolute;
            bottom: 0;
            border-top: 1px solid #e7e9eb;
            padding: 8px 0;
            text-align: center;
        }

        table.billing td {
            background-color: #fff;
        }

        table td#invoiced_to {
            text-align: left;
            padding-left: 0;
        }

        #notes {
            color: #767676;
            font-size: 11px;
        }

        .item-summary {
            font-size: 11px;
            padding-left: 0;
        }


        .page_break {
            page-break-before: always;
        }


        table td.text-center {
            text-align: center;
        }

        .word-break {
            word-wrap: break-word;
        }

        #invoice-table td {
            border: 1px solid #e7e9eb;
        }

        .border-left-0 {
            border-left: 0 !important;
        }

        .border-right-0 {
            border-right: 0 !important;
        }

        .border-top-0 {
            border-top: 0 !important;
        }

        .border-bottom-0 {
            border-bottom: 0 !important;
        }

    </style>
</head>

<body>
    <header class="clearfix">
        <table cellpadding="0" cellspacing="0" class="billing">
            <tr>
                <td colspan="2">
                    <h1>Invoice</h1>
                </td>
            </tr>
            <tr>
                <td id="invoiced_to">
                    <div>
                        <small>Billed To:</small><br>
                        {{ ucwords($payment->user->name) }}<br>
                        {{$payment->user->email}}<br>
                    </div>
                </td>
                <td>
                    <div id="company">
                        <div id="logo">
                            <img src="{{asset('uploads/'.App\Models\InvoiceSetting::getInvoiceSetting('invoice_logo'))}}" alt="home" class="dark-logo" />
                        </div>
                        <small>Generated By:</small>
                        <div>{{App\Models\Setting::getSetting('app_name')}}</div>
                        <div>{!! nl2br(App\Models\Setting::getSetting('address')) !!}</div>
                        <div>{{App\Models\Setting::getSetting('phone_no')}}</div>
                    </div>
                </td>
            </tr>
        </table>
    </header>
    <main>
        <div id="details">
            <div id="invoice">
                <h1>{{App\Models\InvoiceSetting::getInvoiceSetting('invoice_prefix')}}#{{str_pad($payment->id, App\Models\InvoiceSetting::getInvoiceSetting('invoice_number_digit'), '0', STR_PAD_LEFT)}}</h1>
                
                <div class="date">Invoice Date: {{date('d-m-Y',strtotime($payment->created_at))}}</div>
                <div class="">Status: paid</div>
            </div>
        </div>
        <table cellspacing="0" cellpadding="0" id="invoice-table">
            <thead>
                <tr>
                    <th class="no">#</th>
                    <th class="desc">Item</th>
                    <th class="qty">Qty</th>
                    <th class="unit">Price (USD)</th>
                </tr>
            </thead>
            <tbody>
                <tr style="page-break-inside: avoid;">
                    <td class="no">1</td>
                    <td class="desc">
                        <h3>{{ucfirst($payment->plan->plan_name)}}</h3>
                    </td>
                    <td class="qty">
                        <h3>1</h3>
                    </td>
                    <td class="unit">{{$payment->plan->plan_price}}</td>
                </tr>
            </tbody>
            <tfoot>
                <tr dontbreak="true">
                    <td colspan="3">Total Paid</td>
                    <td style="text-align: center">{{$payment->plan->plan_price}}</td>
                </tr>
            </tfoot>
        </table>

        <p id="notes" class="word-break">
            Terms And Condition <br>{{App\Models\InvoiceSetting::getInvoiceSetting('terms_and_conditions')}}
        </p>

        <div class="b-all m-t-20 m-b-20 text-center">
            <h3 class="box-title m-t-20 text-center h3-border">Payments</h3>
            <div class="row">
                <div class="col-sm-12">
                    <div class="table-responsive m-t-40" style="clear: both;">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th class="text-center">#</th>
                                    <th class="text-center">Price</th>
                                    <th class="text-center">Payment Type</th>
                                    <th class="text-center">Paid On</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td class="text-center">1</td>
                                    <td class="text-center">{{$transaction->total_paid}} USD</td>
                                    <td class="text-center">{{$transaction->payment_type}}</td>
                                    <td class="text-center">
                                    {{date('d-m-Y',strtotime($transaction->date))}}
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </main>
</body>
</html>
