import { ILoja } from 'app/entities/loja/loja.model';
import { ICliente } from 'app/entities/cliente/cliente.model';

export interface ICompra {
  id?: number;
  nomeProduto?: string;
  valor?: number;
  lojas?: ILoja[];
  clientes?: ICliente[];
}

export class Compra implements ICompra {
  constructor(
    public id?: number,
    public nomeProduto?: string,
    public valor?: number,
    public lojas?: ILoja[],
    public clientes?: ICliente[]
  ) {}
}

export function getCompraIdentifier(compra: ICompra): number | undefined {
  return compra.id;
}
