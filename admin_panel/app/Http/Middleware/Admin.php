<?php 

namespace App\Http\Middleware;

use Closure;
use Illuminate\Support\Facades\Auth;
use App\Providers\RouteServiceProvider;
use Illuminate\Http\Request;

class Admin 
{
    public function handle(Request $request, Closure $next)
    {
        if(Auth::user()->user_type == "Super Admin" || Auth::user()->user_type == "Demo")
        {
            return $next($request);
        }

        return redirect(RouteServiceProvider::HOME);
    }
}