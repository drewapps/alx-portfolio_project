<?php

namespace App\Mail;

use App\Models\SupportTicket;
use Illuminate\Mail\Mailable;
use Illuminate\Queue\SerializesModels;

class ReplySupportTicketMail extends Mailable
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
    public $name;
    public function __construct($ticket_id,$name,$message)
    {
        $this->support_ticket_data = SupportTicket::where('ticket_id',$ticket_id)->first();
        $this->message = $message;
        $this->ticket = $ticket_id;
        $this->name = $name;
    }

    /**
     * Build the message.
     *
     * @return $this
     */
    public function build()
    {
        return $this->from(env('MAIL_FROM_ADDRESS'))->subject('Reply Support Ticket. Ticket ID: ' .$this->ticket)->markdown('emails.reply_support_ticket');
    }
}
