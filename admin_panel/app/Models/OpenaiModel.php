<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class OpenaiModel extends Model
{
    use HasFactory;
    protected $table = "openai_model";

    protected $fillable = [
        "name"
    ];
}
