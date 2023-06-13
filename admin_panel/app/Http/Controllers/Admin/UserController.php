<?php

namespace App\Http\Controllers\Admin;

use Carbon\Carbon;
use App\Models\Plan;
use App\Models\User;
use App\Models\AIImage;
use App\Models\Invoice;
use App\Models\Setting;
use App\Models\Documents;
use App\Models\Transaction;
use Illuminate\Support\Str;
use App\Models\Subscription;
use Illuminate\Http\Request;
use App\Models\SupportTicket;
use App\Models\SupportMessage;
use App\Models\FavoriteTemplate;
use App\Models\UserDeleteRequest;
use Spatie\Permission\Models\Role;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Validator;

class UserController extends Controller
{
    public function index(Request $request)
    {
        if($request->search)
        {
            $index['data'] = User::where('name','like', '%'.$request->search.'%')
            ->orWhere('email','like', '%'.$request->search.'%')
            ->select('id','name','email','is_subscribe','user_type','image','created_at','status')->orderBy('id', 'desc')->paginate(10);
            $index['search'] = $request->search;
            return view("user.index", $index);
        }
        else
        {
            $index['data'] = User::select('id','name','email','is_subscribe','user_type','image','created_at','status')->orderBy('id', 'desc')->paginate(10);
            $index['search'] = "";
            return view("user.index", $index);
        }
    }

    public function create()
    {
        return view("user.create");
    }

    public function password_change(Request $request)
    {
        $user = User::find($request->get("user_id"));
        $user->password = bcrypt($request->get("passwd"));
        $user->save();
    }

    public function user_status(Request $request)
    {
        $user = User::find($request->get("id"));
        $user->status = ($request->get("checked")=="true")?1:0;
        $user->save();
    }

    public function show($id)
    {
        $user_data = User::find($id);
        if($user_data->email != "demo2023@gmail.com")
        {
            $index['data'] = User::find($id);
            // $index['subscription'] = Subscription::where('status',1)->get();
            $index['subscription'] = Plan::where('status',1)->get();
            $index['transaction'] = Transaction::where('user_id',$id)->get();
            $index['document'] = Documents::where('user_id',$id)->orderBy('id', 'DESC')->get();
            $index['image'] = AIImage::where('user_id',$id)->orderBy('id', 'DESC')->get();

            $documents = Documents::where('user_id',$id)->whereMonth('created_at', Carbon::now()->month)->get();
            $index['documents_created'] = $documents->count();
            $count = 0;
            foreach($documents as $total)
            {
                $count = $count + $total->document_word;
            }
            $index['documents_word'] = $count;
            $index['templates_used'] = $documents->groupby('templates_id')->count();

            $image = AIImage::where('user_id',$id)->whereMonth('created_at', Carbon::now()->month)->get();
            $index['images_created'] = $image->count();
        }
        return view('user.show',$index);
    }

    public function ai_image_delete(Request $request)
    {
        AIImage::find($request->id)->delete();

        return back();
    }

    public function document_delete(Request $request)
    {
        Documents::find($request->id)->delete();

        return back();
    }

    public function subscription_update(Request $request)
    {
        $validation = Validator::make($request->all(), [
            'plan' => 'required',
            "subscription_start_date" => 'required',
            "subscription_end_date" => 'required',
        ]);

        if ($validation->fails()) {
            return back()->withErrors($validation)->withInput();
        } else {
            $subscription = Subscription::find($request->get("plan"));

            $user = User::find($request->get("id"));
            $user->subscription_id = $request->get("plan");
            $user->subscription_start_date = Carbon::createFromFormat('d M, y',$request->get("subscription_start_date"))->format('Y-m-d');
            $user->subscription_end_date = Carbon::createFromFormat('d M, y',$request->get("subscription_end_date"))->format('Y-m-d');
            $user->is_subscribe = 1;
            $user->save();

            return redirect()->back();
        }
    }

    // public function get_plan(Request $request)
    // {
    //     $data = Subscription::find($request->id);
    //     $detail['start_date'] = date('d M, y');
    //     $detail['end_date'] = date('d M, y', strtotime($data->duration." ".$data->duration_type));
    //     return $detail;
    // }

    public function store(Request $request)
    {
        //dd($request->all());
        $validation = Validator::make($request->all(), [
            'name' => 'required',
            'password' => 'required|min:8',
            // "mobile_no" => 'required|numeric',
            'email' => 'required|email|unique:users,email,' . \Request::get("id"),
            "image" => "nullable|mimes:jpg,png,jpeg",
        ]);

        if ($validation->fails()) {
            return back()->withErrors($validation)->withInput();
        } 
        else 
        {
            // $subscription = Subscription::where('plan_type','trial')->first();

            $id = User::create([
                "name" => $request->get("name"),
                "email" => $request->get("email"),
                "password" => bcrypt($request->get("password")),
                "email_verified_at" => date('Y-m-d H:i:s'),
                "user_type" => "User",
                'words_left' => Setting::getSetting('register_user_default_word'),
                'image_left' => Setting::getSetting('register_user_default_image'),
            ])->id;

            if ($request->file("image") && $request->file('image')->isValid()) {
                $this->upload_image($request->file("image"),"image", $id);
            }
           
            return redirect()->route("user.index");
        }
    }

    public function update(Request $request, $id)
    {
        $validation = Validator::make($request->all(), [
            'name' => 'required',
            //'password' => 'required|min:8',
            "words_left" => 'required|numeric',
            "image_left" => 'required|numeric',
            'email' => 'required|email|unique:users,email,' . \Request::get("id"),
            "image" => "nullable|mimes:jpg,png,jpeg",
        ]);

        if ($validation->fails()) {
            return back()->withErrors($validation)->withInput();
        } 
        else 
        {
            $user = User::whereId($request->get("id"))->first();
            $user->name = $request->get("name");
            $user->email = $request->get("email");
            $user->words_left = $request->get("words_left");
            $user->image_left = $request->get("image_left");
            $user->subscription_start_date = Carbon::createFromFormat('d M, y',$request->get("start_date"))->format('Y-m-d');
            $user->subscription_end_date = Carbon::createFromFormat('d M, y',$request->get("end_date"))->format('Y-m-d');
            if(!empty($request->get("password")))
            {
                $user->password = bcrypt($request->get("password"));
            }
            $user->save();
            
            if ($request->file("image") && $request->file('image')->isValid()) {
                $this->upload_image($request->file("image"),"image", $id);
                unlink('./uploads/'.$user->image);
            }

            return redirect()->back();
        }
    }

    public function destroy($id)
    {
        $user = User::find($id);
        if($user->user_type != "Super Admin")
        {
            Transaction::where("user_id",$id)->delete();
            AIImage::where("user_id",$id)->delete();
            Documents::where("user_id",$id)->delete();
            FavoriteTemplate::where("user_id",$id)->delete();
            Invoice::where("user_id",$id)->delete();
            UserDeleteRequest::where("user_id",$id)->delete();
            $ticket = SupportTicket::where('user_id', $id)->get(); 
            foreach($ticket as $user_ticket)
            {
                $user_ticket->delete();
                SupportMessage::where('ticket_id', $user_ticket->ticket_id)->delete(); 
            }
            User::find($id)->delete();
            
            return redirect()->route('user.index');
        }

        return redirect()->route('user.index');
    }

    private function upload_image($file,$field,$id)
    {
        $destinationPath = './uploads';
        $extension = $file->getClientOriginalExtension();
        $fileName = Str::uuid() . '.' . $extension;
        $file->move($destinationPath, $fileName);
        
        $image = User::find($id);
        $image->$field = $fileName;
        $image->save();
    }
}
