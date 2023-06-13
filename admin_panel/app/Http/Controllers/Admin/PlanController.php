<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\Plan;
use App\Models\User;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Str;
use Auth;

class PlanController extends Controller
{
    public function index()
    {
        $index['data'] = Plan::get();
        return view("plan.index", $index);
    }

    public function create()
    {
        return view("plan.create");
    }

    public function show($id)
    {
        $index['data'] = Plan::find($id);
        return view('plan.show',$index);
    }

    public function plan_status(Request $request)
    {
        $plan = Plan::find($request->get("id"));
        $plan->status = ($request->get("checked")=="true")?1:0;
        $plan->save();
    }

    public function plan_most_popular(Request $request)
    {
        $sub = Plan::get();
        foreach($sub as $s)
        {
            $plan = Plan::find($s->id);
            $plan->most_popular = 0;
            $plan->save();
        }
        $plan = Plan::find($request->get("id"));
        $plan->most_popular = ($request->get("checked")=="true")?1:0;
        $plan->save();
    }

    public function store(Request $request)
    {
        $validation = Validator::make($request->all(), [
            'plan_name' => 'required',
            'plan_price' => 'required',
            "total_words" => 'required',
            'number_of_images' => 'required',
        ]);

        if ($validation->fails()) {
            return back()->withErrors($validation)->withInput();
        } 
        else 
        {
            $id = Plan::create([
                "plan_name" => $request->get("plan_name"),
                "plan_price" => $request->get("plan_price"),
                "total_words" => $request->get("total_words"),
                "number_of_images" => $request->get("number_of_images"),
                "rewarded_enable" => ($request->get("rewarded_enable"))?1:0,
                "duration" => $request->get("duration"),
                "duration_type" => $request->get("duration_type"),
                "google_product_enable" => ($request->get("google_product_enable"))?1:0,
                "google_product_id" =>  $request->get("google_product_id"),
                "status" => 1,
            ])->id;
           
            return redirect()->route("plan.index");
        }
    }

    public function edit($id)
    {
        $index['plan'] = Plan::find($id);
        return view("plan.edit", $index);
    }

    public function update(Request $request, $id)
    {
        //dd($request->all());
        $validation = Validator::make($request->all(), [
            'plan_name' => 'required',
            'plan_price' => 'required',
            "total_words" => 'required',
            'number_of_images' => 'required',
        ]);

        if ($validation->fails()) {
            return back()->withErrors($validation)->withInput();
        } else {
            $plan = Plan::find($request->get("id"));
            $plan->plan_name = $request->plan_name;
            $plan->plan_price = $request->plan_price;
            $plan->total_words = $request->total_words;
            $plan->number_of_images = $request->number_of_images;
            $plan->rewarded_enable = ($request->rewarded_enable)?1:0;
            $plan->duration = $request->duration;
            $plan->duration_type = $request->duration_type;
            $plan->google_product_enable = ($request->google_product_enable)?1:0;
            $plan->google_product_id = $request->google_product_id;
            $plan->save();

            return redirect()->route('plan.index');
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
        Plan::find($id)->delete();

        return redirect()->route('plan.index');
    }
}
