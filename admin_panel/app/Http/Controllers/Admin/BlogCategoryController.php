<?php

namespace App\Http\Controllers\Admin;

use Illuminate\Support\Str;
use Illuminate\Http\Request;
use App\Models\BlogCategory;
use App\Models\Blog;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\Validator;

class BlogCategoryController extends Controller
{
    public function index()
    {
        $index['data'] = BlogCategory::get();
        return view("blog_category.index", $index);
    }

    public function create()
    {
        return view("blog_category.create");
    }

    public function store(Request $request)
    {
        $validation = Validator::make($request->all(), [
            'name' => 'required',
        ]);

        if ($validation->fails()) {
            return back()->withErrors($validation)->withInput();
        } else {
            $id = BlogCategory::create([
                "name" => $request->get("name"),
                "status" => 1,
            ])->id;

            return redirect()->route("blog-category.index");
        }
    }

    public function blog_category_status(Request $request)
    {
        $category = BlogCategory::find($request->get("id"));
        $category->status = ($request->get("checked")=="true")?1:0;
        $category->save();
    }

    public function edit($id)
    {
        $category = BlogCategory::find($id);
        return view("blog_category.edit", compact("category"));
    }

    public function update(Request $request, $id)
    {
        $validation = Validator::make($request->all(), [
            'name' => 'required',
        ]);

        if ($validation->fails()) {
            return back()->withErrors($validation)->withInput();
        } else {
            $category = BlogCategory::find($request->get("id"));
            $category->name = $request->get("name");
            $category->save();

            return redirect()->route('blog-category.index');
        }
    }

    public function destroy($id)
    {
        $blog = Blog::where('category_id',$id)->get();

        foreach($blog as $b)
        {
            Blog::find($b->id)->delete();
        }

        BlogCategory::find($id)->delete();

        return redirect()->route('blog-category.index');
    }
}
