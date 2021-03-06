/**
 * 扩展静态工具类
 */
jQuery.extend({
    //判断非空
    isEmpty : function (data) {
        return (data == "" || data == undefined || data == null) ? true : false;
    },
    //去除两边空格判断非空
    isBlank : function(data){
        if(this.isNull(data)){
            return true;
        }
        return this.isEmpty($.trim(data));

    },
    //判断非null
    isNull : function (data) {
        return (data == undefined || data == null) ? true : false;
    },

    ajaxPrefix : function (prefix,options) {
        if ( typeof prefix === "object" ) {
            options = prefix;
            prefix = '';
        }
        options.url = prefix+options.url;
        var xClientUsername = sessionStorage.getItem("x-client-username");
        var xClientToken = sessionStorage.getItem("x-client-token");
        if(options.data){
            options.data['username'] = xClientUsername;
            options.data['token'] = xClientToken;
        }else{
            options.data = {
                username : xClientUsername,
                token : xClientToken
            }
        }
        if(typeof options.error != 'function'){
            options.error = function (xhr) {
                var $modal = $('#indexModal');
                $modal.find('.modal-body').text(xhr.responseText);
                $modal.modal('show');
            }
        }
        $.ajax(options);
    },
    /**
     * 默认使用POST JSON形式发送请求，ajax参数可被覆盖 携带token参数
     * @param prefix 请求前缀，默认使用clientPath变量初始值
     * @param options
     */
    ajaxJson : function (prefix,options) {
        if ( typeof prefix === "object" ) {
            options = prefix;
            prefix = '';
        }
        options.url = prefix+options.url;

        $.extend(options, {
            type : 'POST',
            contentType: "application/json"
        });
        var xClientUsername = sessionStorage.getItem("x-client-username");
        var xClientToken = sessionStorage.getItem("x-client-token");
        if(options.data){
            options.data['username'] = xClientUsername;
            options.data['token'] = xClientToken;
        }else{
            options.data = {
                username : xClientUsername,
                token : xClientToken
            }
        }
        options.data = JSON.stringify(options.data);
        if(typeof options.error != 'function'){
            options.error = function (xhr) {
                var $modal = $('#indexModal');
                $modal.find('.modal-body').text(xhr.responseText);
                $modal.modal('show');
            }
        }
        $.ajax(options);
    }
});

/**
 * 扩展对象工具类
 */
+(function ($) {
    //快捷事件绑定
    $.fn.qon = function(events){
        var that = this;
        var $elem = $(this);
        var s = $elem.find('[data-on]'),t;
        s.each(function (i,item) {
            try{
                t = $(item);
                var on = t.data('on');
                var evs = [];
                if(on.indexOf(";") > -1){
                    evs = on.split(';');
                }else{
                    evs[0] = on;
                }
                for(var j in evs) {
                    var p = evs[j].replace(' ','');
                    if (p.indexOf(':') === -1) {
                        console.info('Bind event str should like this [data-on="click:eventName"]');
                        continue;
                    }
                    var sp = p.split(':');
                    if (events[sp[1]] && typeof events[sp[1]] === "function") {
                        //动态子元素选择器
                        var childSelector = t.data('onChild');
                        if (childSelector && childSelector.length > 0) {
                            //动态绑定子元素事件
                            t.on(sp[0], childSelector, events[sp[1]]);
                        } else {
                            //绑定元素事件
                            t.on(sp[0], events[sp[1]]);
                        }
                    } else {
                        console.info('Bind event ' + sp[1] + ' is not defined');
                    }
                }
                return that;
            }catch (e) {
                console.error(e);
                return that;
            }
        });
        return that;
    };

    $.fn.dataAttr = function(key,val){
        var $tis = $(this);
        var g=$tis[0].attributes;

        if(key && key.length === 0){
            return;
        }

        if(val){
            if(key && key.length > 0){
                if($tis.is('['+key+']')){
                    $tis.attr(key,val);
                    return;
                }
                $tis.attr('data-'+key,val);
            }
            return;
        }

        if(key && key.length > 0){
            return $tis.attr(key) || $tis.attr('data-'+key);
        }
        //正则匹配
        var rdashAlpha = /-([a-z])/ig,
            // camelCase替换字符串时的回调函数
            fcamelCase = function( all, letter ) {
                return letter.toUpperCase();
            },
            camelCase = function( string ) {
                return string.replace( rdashAlpha, fcamelCase );
            };

        function get_v(v) {
            if(v.startsWith('{')){
                try {
                    v = (new Function('return '+ v))();
                } catch (e) {
                    console.error(e.toLocaleString());
                }
                return v;
            }
            switch (v) {
                case 'true' : return true;
                case 'false' : return false;
                default:
            }
            return v;
        }

        var h;
        var res = {};
        for(var i=0;i<g.length;i++) {
            h = g[i].name;
            if(h.indexOf("data-")=== 0){
                var nn = camelCase(h.substring(5));
                res[nn] = get_v(g[i].nodeValue);
                continue;
            }
            res[h] = get_v(g[i].nodeValue);
        }
        return res;
    };

    //序列化表单为Json格式数据
    $.fn.serializeJson = function () {
        var o = {};
        var a = this.serializeArray();

        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]]
                }
                o[this.name].push(this.value || '')
            } else {
                o[this.name] = this.value || ''
            }
        });

        return o;
    };
    $.fn.serializeJsonSfy = function () {
        return JSON.stringify(this.serializeJson());
    };
    //便捷查询name的对象
    $.fn.findName = function (name) {
        return $(this).find('[name="'+name+'"]');
    };
    //便捷查询data-id的对象
    $.fn.findDataId = function (id) {
        return $(this).find('[data-id="'+id+'"]');
    };
    //TODO 打开窗口公共方法
    $.fn.openWindow = function (options) {
        var defaults = {
            target : '', //静态模板选择器 仅支持百度template.js  或者请求路径
            url : '', //url 数据加载路径
            height : 376,
            width : 600,
            //关闭前回调 返回false可阻断关闭
            beforeClose : function () {
                return true;
            },
            //关闭后回调
            afterClose : function () {
            }
        };
        var settings = $.extend({},defaults,options);
        var $this = $(this);
        var tpl = '<div class="window-mask">' +
            '<div class="window-container" style="width: {width}px;height: {height}px;max-width: 100%;">' +
            '<i class="fa fa-circle-o-notch fa-spin"></i> 加载页面中...</div>' +
            '</div>';
        tpl = tpl.replaceAll('{width}',settings.width).replaceAll('{height}',settings.height);
        var $mask = $this.append(tpl).find('.window-mask:last-child');
        var $container = $mask.find('.window-container');
        var wh = $(window).height();
        var h = settings.height;
        if(wh<h) {
            h = wh;
        }
        var top = (wh-h)/2;
        $container.css({height:h,top :top-50});
        $container.animate({top:top},100);
        var bindEvent = function () {
            $container.append('<div class="window-close window-close-btn"><i class="fa fa-close"></i></div>');
            //绑定关闭事件
            $container.find('.window-close').off().on('click',function () {
                if(typeof settings.beforeClose === 'function'){
                    if(!settings.beforeClose()){
                        return;
                    }
                }
                var $this = $(this);
                $container.animate({top:'-=50'},100,function () {
                    $this.closest('.window-mask').remove();
                    if(typeof settings.afterClose === 'function'){
                        settings.afterClose();
                    }
                });
            });
        };
        bindEvent();
        try{
            if(settings.target.indexOf('/') >= 0){
                $.ajaxPrefix({
                    url : settings.target,
                    success : function (res) {
                        if(settings.url !== '') {
                            $.ajaxJson({
                                url: settings.url,
                                success: function (r) {
                                    var html = template.render(res, r);
                                    $container.html(html);
                                    bindEvent();
                                }
                            });
                        }else{
                            $container.html(res);
                            bindEvent();
                        }
                    }
                });
            }
            else if (settings.target.startsWith('<')){
                if(settings.url !== '') {
                    $.ajaxJson({
                        url: settings.url,
                        success: function (r) {
                            var html = template.render(settings.target, r);
                            $container.html(html);
                            bindEvent();
                        }
                    });
                }else{
                    $container.html(settings.target);
                    bindEvent();
                }
            }
            else {
                if(settings.url !== '') {
                    $.ajaxJson({
                        url: settings.url,
                        success: function (r) {
                            var html = template.render($(settings.target).html(), r);
                            $container.html(html);
                            bindEvent();
                        }
                    });
                }else{
                    $container.html($(settings.target).html());
                    bindEvent();
                }
            }
        }catch (e) {
            console.error(e);
        }
    }

}(jQuery));


/**
 * 扩展String方法
 */
$.extend(String.prototype, {
    isInteger: function() {
        return (new RegExp(/^\d+$/).test(this))
    },
    isNumber: function(value, element) {
        return (new RegExp(/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/).test(this))
    },
    trim: function() {
        return this.replace(/(^\s*)|(\s*$)|\r|\n/g, '')
    },
    replaceAll: function(os, ns) {
        return this.replace(new RegExp(os,'gm'), ns)
    },
    toBool: function() {
        return (this.toLowerCase() === 'true')
    },
    toJson: function() {
        var json = this;

        try {
            if (typeof json == 'object') json = json.toString()
            if (!json.trim().match("^\{(.+:.+,*){1,}\}$")) return this
            else return JSON.parse(this)
        } catch (e) {
            return this
        }
    },
    toObj: function() {
        var obj = null;
        try {
            obj = (new Function('return '+ this))()
        } catch (e) {
            obj = this;
        }
        return obj
    }
});