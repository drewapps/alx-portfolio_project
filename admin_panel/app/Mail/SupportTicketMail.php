<?php

namespace App\Mail;

use App\Models\SupportTicket;
use Illuminate\Mail\Mailable;
use App\Models\SupportMessage;
use Illuminate\Queue\SerializesModels;

class SupportTicketMail extends Mailable
{
    use SerializesModels;

    /**
     * Create a new message instance.
     *
     * @return void
     */
    public $support_ticket_data;
    public $message;
    public $ticket;
    public $admin_name;
    public function __construct($ticket_id,$name)
    {
        $this->support_ticket_data = SupportTicket::where('ticket_id',$ticket_id)->first();
        $this->message = SupportMessage::where('ticket_id',$ticket_id)->first();
        $this->ticket = $ticket_id;
        $this->admin_name = $name;
    }

    /**
     * Build the message.
     *
     * @return $this
     */
    public function build()
    {
        return $this->from(env('MAIL_FROM_ADDRESS'))->subject('New Support Ticket Created. Ticket ID: ' .$this->ticket)->markdown('emails.support_ticket');
    }
}
