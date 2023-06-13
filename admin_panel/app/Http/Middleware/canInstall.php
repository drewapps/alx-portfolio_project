<?php

namespace App\Http\Middleware;

use Closure;

class canInstall
{
    public function handle($request, Closure $next)
    {
        if ($this->alreadyInstalled()) {
            return redirect('/');
        }

        return $next($request);
    }

    public function alreadyInstalled()
    {
        return (file_exists(storage_path('installed')) && file_get_contents(storage_path('installed')) == "copy_builder1" || file_get_contents(storage_path('installed')) == "copy_builder2");
    }
}
