if exists(select 1 from sysobjects where name = 'pr_rel_mov_pending_old_system')
    begin
        drop procedure pr_rel_mov_pending_old_system;
    end
go

create procedure pr_rel_mov_pending_old_system
as
    begin
		select 
			x.[customer], x.[register_date], x.[register_online], count(*) as count_products
		from 
		(
			select 
				[codigo_loja] as customer, [registro_online] as register_online, convert(varchar(10), [data], 103) as register_date
			from 
				[tb_bin_turno_fechamento_online] with (nolock) 
			where 
				[sincronizado]=0 and [tipo_movimento]='A' and [registro_online]=1
			union all
			select 
				fe.[codigo_loja] as customer, fe.[registro_online] as register_online, convert(varchar(10), fe.[data], 103) as register_date
			from
				[tb_bin_turno_fechamento_online] fe with (nolock) 
			where
				fe.[tipo_movimento]='L'
				and fe.[sincronizado]=0
				and fe.[registro_online]=0
		) x 
		group by 	
		x.[customer], x.[register_date], x.[register_online]
    end
go

/**
exec pr_rel_mov_pending_old_system; 
*/