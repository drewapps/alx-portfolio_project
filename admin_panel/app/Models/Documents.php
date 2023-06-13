<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Documents extends Model
{
    use HasFactory;
    protected $table = "documents";

    protected $fillable = [
        "name","text","user_id","templates_id","document_word"
    ];

    public function user()
    {
        return $this->hasOne("App\Models\User", "id", "user_id");
    }

    public function templates()
    {
        return $this->hasOne("App\Models\Templates", "id", "templates_id");
    }
}
