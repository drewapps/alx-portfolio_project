<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Templates extends Model
{
    use HasFactory;
    protected $table = "templates";

    protected $fillable = [
        "title","description","image","type","slug","status"
    ];
}
