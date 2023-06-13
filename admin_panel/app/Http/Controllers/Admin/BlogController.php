<?php

namespace App\Http\Controllers\Admin;

use Auth;
use App\Models\Blog;
use Illuminate\Support\Str;
use App\Models\BlogCategory;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Validator;

class BlogController extends Controller
{
    public function index()
    {
        $index['data'] = Blog::get();
        return view("blog.index", $index);
    }

    public function create()
    {
        $index['category'] = BlogCategory::where('status',1)->get();
        return view("blog.create",$index);
    }

    public function blog_status(Request $request)
    {
        $blog = Blog::find($request->get("id"));
        $blog->status = ($request->get("checked")=="true")?1:0;
        $blog->save();
    }

    public function store(Request $request)
    {
        $validation = Validator::make($request->all(), [
            'title' => 'required',
            'description' => 'required',
            "meta_description" => 'required',
            'meta_keyword' => 'required',
            "category_id" => 'required',
            "image" =>  "nullable|mimes:jpg,png,jpeg",
        ]);

        if ($validation->fails()) {
            return back()->withErrors($validation)->withInput();
        } else {

            $id = Blog::create([
                "title" => $request->get("title"),
                "slug" => ($request->get("slug"))?Str::slug($request->get("slug")):Str::slug($request->get("title")),
                "description" => $request->get("description"),
                "meta_description" => $request->get("meta_description"),
                "meta_keyword" => $request->get("meta_keyword"),
                "category_id" => $request->get("category_id"),
                "status"=> $request->get("publish"),
            ])->id;

            if ($request->file("image") && $request->file('image')->isValid()) {
                $this->upload_image($request->file("image"),"image", $id);
            }
           
            return redirect()->route("blog.index");
        }
    }

    public function edit($id)
    {
        $index['blog'] = Blog::find($id);
        $index['category'] = BlogCategory::where('status',1)->get();
        return view("blog.edit", $index);
    }

    public function update(Request $request, $id)
    {
        //dd($request->all());
        $validation = Validator::make($request->all(), [
            'title' => 'required',
            'slug' => 'required',
            'description' => 'required',
            "meta_description" => 'required',
            'meta_keyword' => 'required',
            "category_id" => 'required',
            "image" =>  "nullable|mimes:jpg,png,jpeg",
        ]);

        if ($validation->fails()) {
            return back()->withErrors($validation)->withInput();
        } else {
            $blog = Blog::find($request->get("id"));
            $blog->title = $request->title;
            $blog->slug = Str::slug($request->slug);
            $blog->description = $request->description;
            $blog->meta_description = $request->meta_description;
            $blog->meta_keyword = $request->meta_keyword;
            $blog->category_id = $request->category_id;
            $blog->save();

            if ($request->file("image") && $request->file('image')->isValid()) {
                $this->upload_image($request->file("image"),"image", $id);
            }

            return redirect()->route('blog.index');
        }
    }

    public function destroy($id)
    {
        Blog::find($id)->delete();

        return redirect()->route('blog.index');
    }

    private function upload_image($file,$field,$id)
    {
        $destinationPath = './uploads';
        $extension = $file->getClientOriginalExtension();
        $fileName = Str::uuid() . '.' . $extension;
        $file->move($destinationPath, $fileName);
        
        $image = Blog::find($id);
        $image->$field = $fileName;
        $image->save();
    }
}
