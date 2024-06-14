
let code;
const errorCallback = function (args) {
    console.log(args);
    console.log('验证失败');
};
const callback = function (args) {
    console.log(args);
    console.log('验证成功');
};
const expiredCallback = function (args) {
    console.log(args);
    console.log('验证过期');
};
layui.use(['layer', 'util', 'form'], function () {
    const $ = layui.$;
    const layer = layui.layer;
    const util = layui.util;
    const form = layui.form;
    // 事件
    util.on('lay-on', {
        'test-page-custom': function () {
            layer.open({
                type: 1,
                area: '350px',
                resize: false,
                shadeClose: true,
                title: '请验证',
                content: `
                    <div class="layui-form" lay-filter="filter-test-layer" style="margin: 16px;">
                        <div class="layui-form-item">
                            <div class="layui-row">
                                <div class="layui-col-xs7">
                                    <div class="layui-input-wrap">
                                        <div class="layui-input-prefix">
                                            <i class="layui-icon layui-icon-vercode"></i>
                                        </div>
                                        <input type="text" name="captcha" value="" lay-verify="required" placeholder="验证码" lay-reqtext="请填写验证码" autocomplete="off" class="layui-input" lay-affix="clear">
                                    </div>
                                </div>
                                <div class="layui-col-xs5">
                                    <div style="margin-left: 10px;">
                                        <img src="/intercept/verify?diy_name=Bloduc+Spauter" onclick="this.src='/intercept/verify?diy_name=Bloduc+Spauter&&t='+new Date().getTime();" alt="验证码">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="robot"></div>
                        <div class="layui-form-item">
                            <button class="layui-btn layui-btn-fluid" lay-submit lay-filter="demo-login">验证</button>
                        </div>
                    </div>
                `,
                success: function () {
                    // 对弹层中的表单进行初始化渲染
                    form.render();
                    // 初始化reCAPTCHA
                    widgetId = grecaptcha.render('robot', {
                        'sitekey': '6LfMLPcpAAAAAAXj3bvOva08f9edl-KIr5wB2HGb',
                        'callback': callback,
                        'expired-callback': expiredCallback,
                        'error-callback': errorCallback
                    });
                    form.on('submit(demo-login)', function (data) {
                        const robotVerify = getResponseFromRecaptcha();
                        const field = data.field;
                        // 显示填写结果，仅作演示用
                        if (field.captcha == null) {
                            layui.use(['layer'], function () {
                                let layer = layui.layer;
                                layer.msg('请输入验证码！', {
                                    icon: 2,
                                    time: 2000
                                })
                            })
                            return false;
                        }
                        if (robotVerify === false) {
                            layui.use(['layer'], function () {
                                let layer = layui.layer;
                                layer.msg('人机验证失败！', {
                                    icon: 2,
                                    time: 2000
                                })
                            })
                            return false
                        }
                        // 此处可执行 Ajax 等操作
                        axios.get("intercept/getCode?diy_name=Bloduc+Spauter").then(res => {
                            code = res.data
                            if (field.captcha === code) {
                                layui.use(['layer'], function () {
                                    let layer = layui.layer;
                                    layer.msg('验证通过,即将跳转', {
                                        icon: 0,
                                        time: 2000
                                    })
                                })
                                setTimeout(function() {
                                    window.location.href = "../index.html?t=" + new Date().getTime();
                                }, 3000);
                                return false
                            } else {
                                layui.use(['layer'], function () {
                                    let layer = layui.layer;
                                    layer.msg('验证码错误！', {
                                        icon: 2,
                                        time: 2000
                                    })
                                })
                                data.field = ''
                                return false;
                            }
                        })
                        // 阻止默认 form 跳转
                        return false;
                    });
                }
            });
        }
    });
});

const onloadCallback = function () {
    // 此函数现在不再立即初始化recaptcha
};

function getResponseFromRecaptcha() {
    if (typeof grecaptcha !== 'undefined') {
        const responseToken = grecaptcha.getResponse(widgetId);
        if (responseToken.length === 0) {
            alert("验证失败");
            return false
        } else {
            return true
        }
    } else {
        alert("reCAPTCHA未加载");
    }
}