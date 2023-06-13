<?php

namespace App\Http\Controllers\Auth;

use App\Http\Controllers\Controller;
use App\Providers\RouteServiceProvider;
use Illuminate\Foundation\Auth\AuthenticatesUsers;
use Illuminate\Http\Request;
use Auth;   

class LoginController extends Controller
{
    /*
    |--------------------------------------------------------------------------
    | Login Controller
    |--------------------------------------------------------------------------
    |
    | This controller handles authenticating users for the application and
    | redirecting them to your home screen. The controller uses a trait
    | to conveniently provide its functionality to your applications.
    |
    */

    use AuthenticatesUsers;

    /**
     * Where to redirect users after login.
     *
     * @var string
     */
    protected $redirectTo = RouteServiceProvider::HOME;

    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        $this->middleware(['guest','IsInstalled'])->except('logout');
    }

    public function authenticate(Request $request)
    {
        $credentials = array(
            'email' => $request->get('email'),
            'password' => $request->get('password'),
        );

        if (Auth::attempt($credentials)) 
        {
            if(Auth::user() && (Auth::user()->user_type == "Super Admin" || Auth::user()->user_type == "Demo"))
            {
                return redirect('admin/');
            }
            else
            {
                Auth::logout();
                return redirect('/');
            }
        }
        else
        {
            return back()->withErrors("These credentials do not match our records.")->withInput();
        }
    }
}
