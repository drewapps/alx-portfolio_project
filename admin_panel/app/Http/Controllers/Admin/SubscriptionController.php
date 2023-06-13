<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\Subscription;
use App\Models\User;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Str;
use Auth;

class SubscriptionController extends Controller
{
    public function index()
    {
        $index['data'] = Subscription::get();
        return view("subscription.index", $index);
    }

    public function create()
    {
        return view("subscription.create");
    }

    public function show($id)
    {
        $index['data'] = Subscription::find($id);
        return view('subscription.show',$index);
    }

    public function subscription_status(Request $request)
    {
        $subscription = Subscription::find($request->get("id"));
        $subscription->status = ($request->get("checked")=="true")?1:0;
        $subscription->save();
    }

    public function subscription_most_popular(Request $request)
    {
        $sub = Subscription::get();
        foreach($sub as $s)
        {
            $subscription = Subscription::find($s->id);
            $subscription->most_popular = 0;
            $subscription->save();
        }
        $subscription = Subscription::find($request->get("id"));
        $subscription->most_popular = ($request->get("checked")=="true")?1:0;
        $subscription->save();
    }

    public function store(Request $request)
    {
        $validation = Validator::make($request->all(), [
            'plan_name' => 'required',
            'plan_price' => 'required',
            'description' => 'required',
            "discount_price" => 'required',
            'duration' => 'required|numeric',
            "duration_type" => 'required',
            "total_words" => 'required',
            'number_of_images' => 'required',
            'plan_type' => 'required',
            "image" =>  "nullable|mimes:jpg,png,jpeg",
        ]);

        if ($validation->fails()) {
            return back()->withErrors($validation)->withInput();
        } else {

            $id = Subscription::create([
                "plan_name" => $request->get("plan_name"),
                "plan_price" => $request->get("plan_price"),
                "plan_type" => $request->get("plan_type"),
                "description" => $request->get("description"),
                "discount_price" => $request->get("discount_price"),
                "total_words" => $request->get("total_words"),
                "number_of_images" => $request->get("number_of_images"),
                "duration" => $request->get("duration"),
                "duration_type" => $request->get("duration_type"),
                "include_plan_detail" => serialize($request->get('detail')),
                "not_include_plan_detail" => serialize($request->get('detail1')),
            ])->id;

            if ($request->file("image") && $request->file('image')->isValid()) {
                $this->upload_image($request->file("image"),"image", $id);
            }
           
            return redirect()->route("subscription-plan.index");
        }
    }

    public function edit($id)
    {
        $index['subscription'] = Subscription::find($id);
        $index['include_plan_detail'] = unserialize($index['subscription']->include_plan_detail);
        $index['not_include_plan_detail'] = unserialize($index['subscription']->not_include_plan_detail);
        return view("subscription.edit", $index);
    }

    public function update(Request $request, $id)
    {
        //dd($request->all());
        $validation = Validator::make($request->all(), [
            'plan_name' => 'required',
            'plan_price' => 'required',
            'description' => 'required',
            'total_words' => 'required',
            'number_of_images' => 'required',
            "discount_price" => 'required',
            'duration' => 'required|numeric',
            "duration_type" => 'required',
            'plan_type' => 'required',
            "image" =>  "nullable|mimes:jpg,png,jpeg",
        ]);

        if ($validation->fails()) {
            return back()->withErrors($validation)->withInput();
        } else {
            $subscription = Subscription::find($request->get("id"));
            $subscription->plan_name = $request->plan_name;
            $subscription->plan_price = $request->plan_price;
            $subscription->description = $request->description;
            $subscription->total_words = $request->total_words;
            $subscription->number_of_images = $request->number_of_images;
            $subscription->discount_price = $request->discount_price;
            $subscription->duration = $request->duration;
            $subscription->duration_type = $request->duration_type;
            $subscription->plan_type = $request->plan_type;
            $subscription->include_plan_detail = serialize($request->detail);
            $subscription->not_include_plan_detail = serialize($request->detail1);
            $subscription->save();

            if ($request->file("image") && $request->file('image')->isValid()) {
                $this->upload_image($request->file("image"),"image", $id);
            }

            return redirect()->route('subscription-plan.index');
        }
    }

    public function destroy($id)
    {
        $user = User::where("subscription_id",$id)->get();
        foreach($user as $u)
        {
            if($u->user_type != "Super Admin")
            {
                User::find($u->id)->delete();
            }
        }
        Subscription::find($id)->delete();

        return redirect()->route('subscription-plan.index');
    }

    private function upload_image($file,$field,$id)
    {
        $destinationPath = './uploads';
        $extension = $file->getClientOriginalExtension();
        $fileName = Str::uuid() . '.' . $extension;
        $file->move($destinationPath, $fileName);
        
        $image = Subscription::find($id);
        $image->$field = $fileName;
        $image->save();
    }
}
