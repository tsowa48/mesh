package mesh;

/**
 *
 * @author tsowa
 */
public class MeshResponse {
    private Integer status;
    private Object data;

    public MeshResponse(Integer status, Object data) {
      this.status = status;
      this.data = data;
    }
    
    public MeshResponse(Integer status) {
      this.status = status;
      this.data = null;
    }
    
    public MeshResponse(Object data) {
      this.status = 0;
      this.data = data;
    }
    /**
     * @return the status
     */
    public Integer getStatus() {
      return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
      this.status = status;
    }

    /**
     * @return the data
     */
    public Object getData() {
      return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Object data) {
      this.data = data;
    }
  }
