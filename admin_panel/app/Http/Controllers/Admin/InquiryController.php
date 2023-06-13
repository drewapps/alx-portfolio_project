<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\Inquiry;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Str;

class InquiryController extends Controller
{
    public function index()
    {
        $index['data'] = Inquiry::get();
        return view("backend.inquiry", $index);
    }

    public function destroy($id)
    {
        Inquiry::find($id)->delete();
        return redirect()->route('inquiry.index');
    }
}
