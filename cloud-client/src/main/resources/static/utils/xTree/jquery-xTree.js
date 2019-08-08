/*
 * xtree
 * Copyright 2019-2019 Xiann
 * All Rights Reserved.
 * Author: Xiann
 * 详细注释 见文档
 */
+(function ($) {
    //定义Beautifier的构造函数
    var XTree = function(ele, opt) {
        var that = this;
        this.element = ele;
        this.defaults = {
            theme : 'default',
            folderOpenIcon:'folder-open-o', //文件开启图标  图标采用fontawesome  http://www.fontawesome.com.cn/faicons/
            folderCloseIcon:'folder-o', //文件关闭图标
            itemIcon : 'caret-right', //项目文件图标 ，可以自定义
            open : false, //全部展开
            fixed : false, //是否固定菜单，开启此参数，则open始终true 全部展开菜单。收缩菜单功能失效
            single : false, //同级父菜单只允许同时打开一个
            openFirstMenu:false, //默认打开第一个菜单
            leftSwitch : true,
            rightSwitch : false, //是否开启右侧按钮标识
            headerAuto : false, //父菜单 点击事件是否自动触发收缩事件  默认双击收缩菜单，如果指定了headerClick事件，则双击失效
            headerClick : 'func', //父菜单点击事件  配置了此事件 双击收缩菜单事件失效
            itemClick : 'func',  //最后一级级别的项目点击事件
            btnGroup : [],
            menu : [] //menu菜单集合 如果menu传入 则优先是用menu菜单渲染 [{id:'',pid:'',...},{id:'',pid:'',...},...]
        };
        this.itemDefaults = {
            // open : null, //是否默认打开，用于父级菜单 单独设置
            color : '#4A4A4A', //项目颜色自定义
            icon : null, //自定义图标
            title : '' //项目名称
        };
        this.nodes = {};

        var opts = this.options = $.extend({}, this.defaults, opt);
        this.menu = opts.menu;

        var $this = $(ele);
        //开启菜单固定，则open始终true
        if(opts.fixed){
            opts.open = true;
        }
        var tpl = '';

        //组装菜单json数据
        var menu = {};
        menu['top'] = [];

        //组装自定义按钮列表
        var btnGroups = '';
        var btnGroupsHeader = '';
        if(opts.btnGroup && opts.btnGroup.length >0){
            btnGroups += '<div class="xtree-btn-group">';
            btnGroupsHeader+= '<div class="xtree-btn-group">';
            for(var m=0;m<opts.btnGroup.length;m++){
                var btnArgs = opts.btnGroup[m];
                var tpl_ = '<i data-id="xtreeBtn{index}" class="fa fa-{icon}" title="{title}"></i>'
                    .replaceAll('{icon}',btnArgs.icon || 'settings')
                    .replaceAll('{index}',m)
                    .replaceAll('{title}',btnArgs.title || '');
                btnGroups += tpl_;

                if(btnArgs.showHeader){
                    btnGroupsHeader+= tpl_;
                }
                //预绑定事件
                (function(btnArgs,m){
                    $this.off('click','.xtree-btn-group>i[data-id="xtreeBtn'+m+'"]')
                        .on('click','.xtree-btn-group>i[data-id="xtreeBtn'+m+'"]',function () {
                            if(typeof btnArgs.click ==='function'){
                                var args = that.nodes[$(this).parent().parent().dataAttr('id')];
                                btnArgs.click(args);
                            }
                            return false;
                        });
                })(btnArgs,m);
            }
            btnGroups += '</div>';
            btnGroupsHeader += '</div>';
        }

        var tools = {
            getTplByJson : function (di) {
                di = $.extend({},that.itemDefaults, di);
                var children = menu[di.id];
                //存储每个节点的参数
                that.nodes[di.id] = di;
                //文件夹
                if(children && children.length>0){
                    di.open = di.open||false;
                    //如果是一次最多打开一个文件 则全部打开配置失效
                    if(!opts.single){
                        //默认打开方式
                        di.open = opts.open?true:di.open;
                    }
                    //控制左侧第二个按钮 （默认 文件夹的形式）
                    var secondBtn = di.open?opts.folderOpenIcon:opts.folderCloseIcon;
                    secondBtn = di.icon||secondBtn;
                    //控制默认是否显示
                    var openDisplay = di.open?'block':'none';
                    //最左侧按钮默认图标样式
                    var firstBtn = di.open?'minus':'plus';
                    //控制最右侧按钮默认图标样式
                    var switchIcon = di.open?'angle-down':'angle-right';
                    //最右侧和最左侧图标模板组装
                    var rightSwitch = opts.rightSwitch?'<i class="switch-icon fa fa-{switchIcon}"></i>':'';
                    var leftSwitch = opts.leftSwitch?'<i class="fa fa-{firstBtn}-square-o plus-toggle"></i>':'';

                    tpl+=('<div class="xtree-folder">'+leftSwitch +
                        '<div class="xtree-header" data-id="{id}" data-pid="{pid}">' +
                        '<i class="fa fa-{secondBtn}" style="color: {color};"></i>'+ //plus-square-o
                        '<span class="xtree-header-name" data-aid="{id}" style="color: {color};"> {title}</span>' +
                        btnGroupsHeader+rightSwitch+
                        '</div>'+
                        '<div class="xtree-content" style="display:{display};">').replaceAll('{title}',di.title)
                        .replaceAll('{id}',di.id)
                        .replaceAll('{pid}',di.pid)
                        .replaceAll('{secondBtn}',secondBtn)
                        .replaceAll('{firstBtn}',firstBtn)
                        .replaceAll('{display}',openDisplay)
                        .replaceAll('{switchIcon}',switchIcon)
                        .replaceAll('{color}',di.color);

                    //递归循环拼接下级菜单
                    for(var j=0;j<children.length;j++){
                        tools.getTplByJson(children[j]);
                    }

                    tpl+='</div></div>';
                    return true;
                }

                var itemIcon = di.icon||opts.itemIcon;
                tpl += ('<div class="xtree-item" data-id="{id}" data-pid="{pid}">' +
                    '<i class="fa fa-{itemIcon}" style="color: {color};"></i>' +
                        '<a data-url="" data-aid="{id}" style="color: {color};" title= "{di.title}"> {title}</a>' +
                    btnGroups+'</div>').replaceAll('{title}',di.title || '')
                    .replaceAll('{id}',di.id)
                    .replaceAll('{pid}',di.pid)
                    .replaceAll('{itemIcon}',itemIcon)
                    .replaceAll('{color}',di.color || '#4A4A4A')
                    .replaceAll('{di.title}',di.name || '');

            },
            getMenuJson : function () {
                var get = function (d) {
                    if(d.pid ==='' || d.pid === undefined || d.pid ===null ){
                        menu['top'].push(d);
                    }else {
                        if(menu[d.pid] === undefined  || menu[d.pid] ===null){
                            menu[d.pid] = [];
                        }
                        menu[d.pid].push(d);
                    }
                };
                //从json数据获取menu
                if(opts.menu && opts.menu.length>0){
                    for(var i=0;i<opts.menu.length;i++){
                        get(opts.menu[i]);
                    }
                    return;
                }
                //从页面形式获取menu
                $this.find('>[data-id]').each(function () {
                    var dd = $(this).dataAttr();
                    that.menu.push(dd);
                    get(dd);
                });
            }

        };

        //初始化菜单数据 menu
        tools.getMenuJson();
        //根据菜单数据获取渲染HTML
        var top = menu['top'];
        for(var i=0;i<top.length;i++){
            //默认打开第一个菜单
            if(i===0 && opts.openFirstMenu){
                top[i].open = true;
            }
            tools.getTplByJson(top[i]);
        }
        //渲染组件
        $this.empty().html(tpl);

        //固定 不绑定收缩事件
        if(opts.fixed){
            return;
        }

        this.hide  = function(header){
            var $header = $(header);
            var $firstBtn = $header.prev();
            var $folder = $header.closest('.xtree-folder');
            var $content = $folder.find('>.xtree-content');
            var id = $header.dataAttr('id');
            $content.hide();
            //记录当前的菜单状态
            that.nodes[id].open=false;
            //控制最前面的图标切换
            $firstBtn.removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
            //控制最右侧的图标
            $header.find('>i.switch-icon').removeClass('fa-angle-down').addClass('fa-angle-right');
            //控制父菜单第二个图标切换，如果有自定义图标 则不变
            if($header.find('>i:first').hasClass('fa-'+opts.folderOpenIcon)) {
                $header.find('>i:first').addClass('fa-' + opts.folderCloseIcon).removeClass('fa-' + opts.folderOpenIcon);
            }

        };
        this.show = function (header) {
            var $header = $(header);
            var $firstBtn = $header.prev();
            var $folder = $header.closest('.xtree-folder');
            var $content = $folder.find('>.xtree-content');
            var id = $header.dataAttr('id');
            $content.show();
            //记录当前的菜单状态
            that.nodes[id].open=true;
            //控制最前面的图标切换
            $firstBtn.removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
            //控制最右侧的图标
            $header.find('>i.switch-icon').removeClass('fa-angle-right').addClass('fa-angle-down');
            //控制父菜单第二个图标切换，如果有自定义图标 则不变
            if($header.find('>i:first').hasClass('fa-'+opts.folderCloseIcon)) {
                $header.find('>i:first').addClass('fa-' + opts.folderOpenIcon).removeClass('fa-' + opts.folderCloseIcon);
            }
        };

        var switchEvent = function(header){
            var $header = $(header);
            var $folder = $header.closest('.xtree-folder');
            var $content = $folder.find('>.xtree-content');
            // var id = $header.dataAttr('id');
            //header 收缩之后产生的事件回调
            // if(typeof opts.headerChange === 'function'){
            //     var args = that.nodes[id];
            //     if(opts.headerChange(args) === false){
            //         return;
            //     }
            // }
            //内容隐藏显示切换
            if(!$content.is(':hidden')){
                that.hide(header);
                return;
            }
            that.show(header);
            //一次只允许打开一个父菜单
            if(opts.single){
                $folder.siblings().each(function () {
                    var $sc = $(this).find('>.xtree-content');
                    var $sh = $(this).find('>.xtree-header');
                    if(!$sc.is(':hidden')){
                        $sc.hide();
                        //控制父菜单第二个图标切换，如果有自定义图标 则不变
                        if($sh.find('>i:first').hasClass('fa-'+opts.folderOpenIcon)) {
                            $sh.find('>i:first').addClass('fa-' + opts.folderCloseIcon).removeClass('fa-' + opts.folderOpenIcon);
                        }
                        $sh.find('>i.switch-icon').removeClass('fa-angle-down').addClass('fa-angle-right');
                        $sh.prev().removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                    }
                });
            }
        };

        this.switchEvent = switchEvent;

        //如果左边收缩按钮存在 则绑定事件
        if(opts.leftSwitch) {
            $this.off('click','.plus-toggle').on('click','.plus-toggle', function () {
                var $header = $(this).next();
                switchEvent($header.get(0));
            });
        }
        //绑定header点击事件
        if(typeof opts.headerClick === 'function') {
            $this.off('click', '.xtree-header').on('click', '.xtree-header', function () {
                var $header = $(this);
                //header点击事件回调
                var args = that.nodes[$header.dataAttr('id')];
                if (opts.headerClick(args) === false) {
                    return;
                }
                if (opts.headerAuto){
                    switchEvent(this);
                }
            });
        }else {
            //绑定双击收缩事件
            $this.off('dblclick','.xtree-header').on('dblclick','.xtree-header',function () {
                switchEvent(this);
            });
        }

        //item点击事件回调
        if(typeof opts.itemClick === 'function'){
            $this.off('click','.xtree-item').on('click','.xtree-item',function () {
                var args = that.nodes[$(this).dataAttr('id')];
                opts.itemClick(args);
            });
        }

        //将默认打开第一个按钮设为false，防止后续方法操作，每次都打开第一个菜单
        this.options.openFirstMenu = false;
    };
    //定义Beautifier的方法
    XTree.prototype = {
        add : function(menu){
            var mo = {};
            if(menu && menu.length >0){
                $.each(menu,function (i,item) {
                    mo[item.id] = item;
                });
                var menuOp = $.extend({},this.nodes,mo);
                var mergeMenu = [];
                $.each(menuOp,function (key,val) {
                    mergeMenu.push(val);
                });

                this.reload(mergeMenu);
            }
        },
        /**
         * 修改节点数据 title数据
         * @param id 节点ID
         * @param val 节点值
         */
        modifyTitle : function(id,val){
            if(id === '' || id == null){
                return;
            }
            var $this = $(this.element);
            var $item = $this.find('[data-aid="' + id + '"]');
            if(val && val.length>0){
                this.nodes[id].title = val;
                $item.html(val);
            }
        },
        /**
         * 修改节点数据 name数据
         * @param id 节点ID
         * @param val 节点值
         */
        modifyName : function(id,val){
            if(id === '' || id == null){
                return;
            }
            var $this = $(this.element);
            var $item = $this.find('[data-aid="' + id + '"]');
            if(val && val.length>0){
                this.nodes[id].name = val;
                $item.attr('title',val);
            }
        },
        remove : function(id){
            if(id === '' || id == null){
                return;
            }
            var nodes = this.nodes;
            var ids = {id:true};
            var i = 0;
            do {
                i = 0;
                $.each(nodes, function (key, val) {
                    if (val.id === id) {
                        delete nodes[key];
                        i++;
                    }
                    if(ids[val.pid]){
                        ids[val.id] = true;
                        delete nodes[key];
                        i++;
                    }
                });
            } while (i!=0);

            var menu = [];
            $.each(nodes,function (key,val) {
                menu.push(val);
            });
            this.reload(menu);
        },
        close : function(id){
            var $this = $(this.element);
            if(this.nodes[id] && this.nodes[id].open === true) {
                this.switchEvent($this.find('.xtree-header[data-id="' + id + '"]')[0]);
            }
        },
        open : function(id){
            var $this = $(this.element);
            if(this.nodes[id] && this.nodes[id].open === false) {
                this.switchEvent($this.find('.xtree-header[data-id="' + id + '"]')[0]);
            }
        },
        openAll : function(){
            var that = this;
            var $this = $(this.element);
            $.each(this.nodes,function (key,val) {
                if(val.open === false){
                    that.show($this.find('.xtree-header[data-id="' + val.id + '"]')[0]);
                }
            });
        },
        closeAll : function(){
            var that = this;
            var $this = $(this.element);
            $.each(this.nodes,function (key,val) {
                if(val.open === true){
                    that.hide($this.find('.xtree-header[data-id="' + val.id + '"]')[0]);
                }
            });
        },
        getNodes: function() {
            var nodes = this.nodes;
            var menu = [];
            $.each(nodes,function (key,val) {
                menu.push(val);
            });
            return menu;
        },
        reload : function (menu) {
            if(!menu){
                return;
            }
            this.destroy();
            var options = this.options;
            var $this = $(this.element);
            if(menu){
                options.menu = menu;
            }
            $this.xTree(options);
        },
        destroy : function () {
            var $this = $(this.element);
            $this.data('data-xtree-id',null);
            $this.empty();
        }
    };


    $.fn.xTree = function (option) {
        var args = Array.apply(null, arguments);
        args.shift();
        var internal_return;
        this.each(function () {
            var $this = $(this),
                data = $this.data('data-xtree-id'),
                options = typeof option === 'object' && option;
            if (!data) {
                $this.data('data-xtree-id', (data = new XTree(this, $.extend({}, $this.dataAttr(), options))));
            }

            if (typeof option === 'string' && typeof data[option] === 'function') {
                internal_return = data[option].apply(data, args);
                if (internal_return !== undefined) {
                    return false;
                }
            }
        });
        if (internal_return !== undefined)
            return internal_return;
        else
            return this;
    };
})(jQuery);