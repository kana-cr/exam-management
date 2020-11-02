package us.sep.util.utils;

import java.util.Collections;
import java.util.List;
/**
 * List分页工具类
 *
 * @author zjb
 * @version : PageUtil.java 2020/3/6 13:05 zjb
 */
public class PageUtil<T> {

    /**
     * 总数据
     */
    private Integer totalElements;

    /**
     * 总页码
     */
    private Integer totalPages;
    
    /**
     * 每页大小
     */
    private Integer size;

    /**
     * 当前页
     */
    private Integer number;

    /**
     * 当前页条数
     */
    private Integer numberOfElements;

    /**
     * 是否是首页
     */
    private Boolean first;

    /**
     * 是否是尾页
     */
    private Boolean end;

    /**
     * 上一页
     */
    private int lastPage;


    /**
     * 下一页
     */
    private int nextPage;

    /**
     * 数据
     */
    private List<T> data;





    public PageUtil(List<T> data,int number,int size) {
        if (data == null) {
            this.data = Collections.emptyList();
        }
        if(number < 0 || size < 0 ){
            throw new IllegalArgumentException("参数错误");
        }

        this.data = data;
        this.size = size;
        this.totalElements = data.size();
        this.totalPages = (totalElements + size - 1) / size;
        this.lastPage = Math.max(number - 1, 1) > totalPages ? totalPages - 1 : Math.max(number - 1, 1);
        this.nextPage = number>=totalPages? totalPages: number + 1;
        this.number = Math.min(this.lastPage + 1, this.nextPage);
        this.first = this.lastPage == this.number;
        this.end = this.nextPage == this.number;
        this.numberOfElements = getData().size();

    }


    public List<T> getData() {
        if (data == null) {
            return Collections.emptyList();
        }
        int fromIndex = (number - 1) * size;
        if (fromIndex >= data.size()) {
            //空数组
            return Collections.emptyList();
        }
        if(fromIndex<0){
            return Collections.emptyList();
        }
        int toIndex = number * size;
        if (toIndex >= data.size()) {
            toIndex = data.size();
        }
        return data.subList(fromIndex, toIndex);
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public Boolean getFirst() {
        return first;
    }

    public Boolean getEnd() {
        return end;
    }

    public int getLastPage() {
        return lastPage;
    }

    public int getNextPage() {
        return nextPage;
    }
}