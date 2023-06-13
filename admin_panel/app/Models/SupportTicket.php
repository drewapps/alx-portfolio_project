<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class SupportTicket extends Model
{
    use HasFactory;
    protected $table = "support_ticket";

    protected $fillable = [
        "user_id","priority","ticket_id","category","subject","resolved_on","status"
    ];

    public function user()
    {
        return $this->hasOne("App\Models\User", "id", "user_id");
    }
}
