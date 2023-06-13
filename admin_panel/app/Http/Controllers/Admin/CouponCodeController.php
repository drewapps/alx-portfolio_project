<?php

namespace App\Http\Controllers\Admin;

use App\Models\Setting;
use App\Models\CouponCode;
use Illuminate\Support\Str;
use App\Models\Subscription;
use Illuminate\Http\Request;
use App\Models\CategoryFrame;
use App\Models\FestivalsFrame;
use App\Models\CustomPostFrame;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Validator;

class CouponCodeController extends Controller
{
    public function index()
    {
        $index['data'] = CouponCode::get();
        return view("coupon_code.index", $index);
    }

    public function create()
    {
        $index['subscription'] = Subscription::where('status',1)->get();
        return view("coupon_code.create",$index);
    }

    public function store(Request $request)
    {
        $validation = Validator::make($request->all(), [
            'code' => 'required',
            'discount' => 'required',
            'limit' => 'required',
            'title' => 'required',
            'description' => 'required',
            'valid_until' => 'required',
            'subscription_id' => 'required',
        ]);

        if ($validation->fails()) {
            return back()->withErrors($validation)->withInput();
        } else {
            CouponCode::create([
                "code" => $request->get("code"),
                "discount" => $request->get("discount"),
                "limit" => $request->get("limit"),
                "title" => $request->get("title"),
                "description" => $request->get("description"),
                "valid_until" => $request->get("valid_until"),
                'subscription_id' => $request->get("subscription_id"),
            ]);

            return redirect()->route("coupon-code.index");
        }
    }

    public function coupon_code_status(Request $request)
    {
        $couponCode = CouponCode::find($request->get("id"));
        $couponCode->status = ($request->get("checked")=="true")?1:0;
        $couponCode->save();
    }

    public function edit($id)
    {
        $couponCode = CouponCode::find($id);
        $subscription = Subscription::where('status',1)->get();
        return view("coupon_code.edit", compact("couponCode","subscription"));
    }

    public function update(Request $request, $id)
    {
        $validation = Validator::make($request->all(), [
            'code' => 'required',
            'discount' => 'required',
            'limit' => 'required',
            'title' => 'required',
            'description' => 'required',
            'valid_until' => 'required',
            'subscription_id' => 'required',
        ]);

        if ($validation->fails()) {
            return back()->withErrors($validation)->withInput();
        } else {
            $couponCode = CouponCode::find($request->get("id"));
            $couponCode->code = $request->get("code");
            $couponCode->discount = $request->get("discount");
            $couponCode->limit = $request->get("limit");
            $couponCode->subscription_id = $request->get("subscription_id");
            $couponCode->title = $request->get("title");
            $couponCode->description = $request->get("description");
            $couponCode->valid_until = $request->get("valid_until");
            $couponCode->save();

            return redirect()->route('coupon-code.index');
        }
    }

    public function destroy($id)
    {
        CouponCode::find($id)->delete();
        return redirect()->route('coupon-code.index');
    }
}
