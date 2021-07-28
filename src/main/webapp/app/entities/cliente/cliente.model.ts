import { ICompra } from 'app/entities/compra/compra.model';

export interface ICliente {
  id?: number;
  nome?: string;
  endereco?: string | null;
  telefone?: string | null;
  compra?: ICompra | null;
}

export class Cliente implements ICliente {
  constructor(
    public id?: number,
    public nome?: string,
    public endereco?: string | null,
    public telefone?: string | null,
    public compra?: ICompra | null
  ) {}
}

export function getClienteIdentifier(cliente: ICliente): number | undefined {
  return cliente.id;
}
