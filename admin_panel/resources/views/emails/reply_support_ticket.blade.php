@component('mail::message')
# Reply Support Ticket.

Dear {{$name}},

	Reply Support Ticket. Below are the details for your Support Ticket.

@component('mail::table')

<table>
	<tr><td>Ticket Id: </td><td>{{$support_ticket_data->ticket_id}}</td></tr>
	<tr><td>Status: </td><td>{{$support_ticket_data->status}}</td></tr>
	<tr><td>Subject: </td><td>{{$support_ticket_data->subject}}</td></tr>
	<tr><td>Category: </td><td>{{$support_ticket_data->category}}</td></tr>
	<tr><td>Priority: </td><td>{{$support_ticket_data->priority}}</td></tr>
	<tr><td>Message: </td><td>{{$message}}</td></tr>
</table>

@endcomponent

Thanks,<br>
{{ config('app.name') }}
@endcomponent
