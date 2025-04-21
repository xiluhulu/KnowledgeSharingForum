package com.fgh.www.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.common.util.R;
import com.fgh.www.pojo.Advertisement;
import com.fgh.www.service.IAdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 广告表 前端控制器
 * </p>
 *
 * @author fgh
 * @since 2025-02-25
 */
@RestController
@RequestMapping("/admin-api/advertisement")
public class AAdvertisementController {
    @Autowired
    private IAdvertisementService advertisementService;
    @PostMapping("/indexAdd")
    public R add(@RequestBody Advertisement advertisement) {
        advertisement.setAdvertisementType("主页广告");
        long count = advertisementService.indexCount();
        if(count>=2){
            return R.error("最多只能添加两个广告");
        }
        boolean b = advertisementService.save(advertisement);
        return b?R.ok("添加成功"):R.error();
    }
    @PutMapping("/indexUpdate")
    public R update(@RequestBody Advertisement advertisement) {
        boolean b = advertisementService.updateById(advertisement);
        return b?R.ok("修改成功"):R.error();
    }
    @DeleteMapping("/indexDelete/{id}")
    public R deleteById(@PathVariable(name = "id") Integer id) {
        boolean b = advertisementService.removeById(id);
        return b?R.ok("删除成功"):R.error();
    }
    @GetMapping("/indexSelectPage")
    public R indexSelectPage(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<Advertisement> page = advertisementService.indexSelectPage(pageNum, pageSize);
        return R.ok().put("data", page);
    }
    @PostMapping("/personAdd")
    public R personAdd(@RequestBody Advertisement advertisement) {
        advertisement.setAdvertisementType("个人中心广告");
        long count = advertisementService.personCount();
        if(count>=1){
            return R.error("最多只能添加一个广告");
        }
        boolean b = advertisementService.save(advertisement);
        return b?R.ok("添加成功"):R.error();
    }
    @PutMapping("/personUpdate")
    public R personUpdate(@RequestBody Advertisement advertisement) {
        boolean b = advertisementService.updateById(advertisement);
        return b?R.ok("修改成功"):R.error();
    }
    @DeleteMapping("/personDelete/{id}")
    public R personDelete(@PathVariable(name = "id") Integer id) {
        boolean b = advertisementService.removeById(id);
        return b?R.ok("删除成功"):R.error();
    }
    @GetMapping("/personSelectPage")
    public R personSelectPage(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<Advertisement> page = advertisementService.personSelectPage(pageNum, pageSize);
        return R.ok().put("data", page);
    }
}
