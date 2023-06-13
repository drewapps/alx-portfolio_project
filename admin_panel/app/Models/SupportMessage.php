<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class SupportMessage extends Model
{
    use HasFactory;
    protected $table = "support_messages";
    
    protected $fillable = [
        'user_id', 'ticket_id', 'message', 'attachment'
    ];

    public function user()
    {
        return $this->hasOne("App\Models\User", "id", "user_id");
    }
}
