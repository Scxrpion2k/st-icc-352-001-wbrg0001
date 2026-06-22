package edu.pucmm.eict.Clases;

public class CarroCompraCliente {
    private Usuario usuario;
    private CarroCompra carrito;

    public CarroCompraCliente(Usuario usuario, CarroCompra carrito) {
        this.usuario = usuario;
        this.carrito = carrito;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public CarroCompra getCarrito() {
        return carrito;
    }

    public void setCarrito(CarroCompra carrito) {
        this.carrito = carrito;
    }
}
