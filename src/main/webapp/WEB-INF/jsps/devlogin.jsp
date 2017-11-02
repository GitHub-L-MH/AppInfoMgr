<%--
  Created by IntelliJ IDEA.
  User: L-PC
  Date: 2017/11/2
  Time: 12:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Title</title>
    <link href="${pageContext.request.contextPath }/statics/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath }/statics/css/datepicker3.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath }/statics/css/styles.css" rel="stylesheet">
</head>
<body>
<div class="row">
    <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 col-md-4 col-md-offset-4">
        <div class="login-panel panel panel-default">
            <div class="panel-heading">Log in</div>
            <div class="panel-body">
                <div>
                    <div id="message" style="color: red;font-weight: 700;font-size: 20px"></div>
                    <fieldset>
                        <div class="form-group">
                            <input class="form-control" id="devCode" name="devCode" type="text" placeholder="请再次输入账号">
                        </div>
                        <div class="form-group">
                            <input class="form-control" id="devPwd" name="devPwd" placeholder="请再次输入密码" type="password" value="">
                        </div>
                        <input type="submit" value="登录" class="btn btn-primary">
                    </fieldset>
                </div>
            </div>
        </div>
    </div><!-- /.col-->
</div><!-- /.row -->
<%@include file="common/pathjsp.jsp"%>
<script src="${pageContext.request.contextPath }/statics/js/bootstrap.min.js"></script>
<script type="text/javascript">
    $(function () {
        $(".btn-primary").click(function () {
            console.log(path);
            $.ajax({
                type:"post",
                url:"../../../devLogin/login.action",
                data:{
                    "devCode":$("#devCode").val(),
                    "devPwd":$("#devPwd").val()
                },
                dataType:"json",
                success:function(data){
                    console.log(data);
                    if (data.status == "200"){
                        $("#message").text("");
                        console.log(path);
                        location.href = "/devSystem/devHome";
                    } else if (data.status == "10001") {
                        $("#message").text(data.message);
                    } else if (data.status == "10002"){
                        $("#message").text(data.message);
                    }
                },
                error:function(data){

                }
            });
        });
    });
</script>
</body>
</html>
