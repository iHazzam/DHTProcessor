@extends('layouts.app')

@section('content')
    <div class="container">
        <div class="row">
            <div class="col-md-8 col-md-offset-2">
                <div class="panel panel-default">
                    <div class="panel-heading">Make Request</div>

                    <div class="panel-body">
                        <h2>Make Requests:</h2>
                        <p><span>Currently this processor offers 4 tasks. You can...</span>
                            <button type="button" class="btn btn-primary paddme" data-toggle="modal" data-target="#modal1">Find out word count, most common word and average word length from a .txt file</button>
                            <button type="button" class="btn btn-success paddme " data-toggle="modal" data-target="#modal2">Find out word count, most common word and average word length from a .docx file</button>
                            <button type="button" class="btn btn-warning paddme" data-toggle="modal" data-target="#modal3">Find out the sum, mean and mode of all numbers in a .xlsx file</button>
                            <button type="button" class="btn btn-danger paddme" data-toggle="modal" data-target="#modal4">Find out the average colour of a picture (currently accepts GIF, PNG, JPEG only) (</button>
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <div id="modal1" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;  </button>
                        <h4 class="modal-title" id="myModalLabel">Please upload a <b>.txt</b> file</h4>
                    </div>
                    <form role="form" method="post" action="request/post/{{Auth::user()->id}}">
                        {{ csrf_field() }}
                        <div class="modal-body">

                                <input type="file" name="fileToUpload" id="fileToUpload">
                                <input name="method" type="hidden" value="1">
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-default">Submit</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div id="modal2" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;  </button>
                        <h4 class="modal-title" id="myModalLabel">Please upload a <b>.docx</b> file</h4>
                    </div>

                    <form role="form" method="post" action="request/post/{{Auth::user()->id}}">
                        {{ csrf_field() }}
                        <div class="modal-body">
                                <input type="file" name="fileToUpload" id="fileToUpload">
                                <input name="method" type="hidden" value="2">
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-default">Submit</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div id="modal3" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;  </button>
                        <h4 class="modal-title" id="myModalLabel">Please upload a <b>.xlsx</b> file</h4>
                    </div>
                    <form role="form" method="post" action="request/post/{{Auth::user()->id}}">
                        {{ csrf_field() }}
                        <div class="modal-body">

                                <input type="file" name="fileToUpload" id="fileToUpload">
                                <input name="method" type="hidden"   value="3">
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-default">Submit</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div id="modal4" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;  </button>
                        <h4 class="modal-title" id="myModalLabel">Please upload a <b>.gif, .png</b> or <b>.jpg</b> file</h4>
                    </div>
                    <form role="form" method="post" action="request/post/{{Auth::user()->id}}">
                        {{ csrf_field() }}
                        <div class="modal-body">

                                <input type="file" name="fileToUpload" id="fileToUpload">
                                <input name="method" type="hidden" value="4">
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-default">Submit</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

    </div>
@endsection
