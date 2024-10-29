if exists(select 1 from sysobjects where name = 'pr_sinc_data_product_movement_database')
    begin
        drop procedure pr_sinc_data_product_movement_database;
    end
go

create procedure pr_sinc_data_product_movement_database
(
    @operation INT,
	@movement_legacy_id BIGINT,
	@sinc BIT,
	@version_register TIMESTAMP
)
as
    begin
		if(4=@operation)
			begin
				SELECT TOP(10)
					m.[moviment_id] as movement_id,
					m.[Id] as product_id, 
					m.[customer] as company_description,
					isnull(m.[CreditIn],0) as credit_in, 
					isnull(m.[CreditOut],0) as credit_out,
					isnull(m.[RdgDateTime],getdate()) as reading_date,
					isnull(m.RstDateTime,m.[RdgDateTime]) as rst_datetime,
					isnull((select top(1) [RdgDateTime] from [QhMaster_Sinc] with (nolock) where id=m.[id] and [RdgDateTime]<m.[RdgDateTime] order by [RdgDateTime] desc),[RstDateTime]) as initial_date,
					isnull(m.[LA],'L') as movement_type,
					isnull(m.[sinc_new],0) as processing, 
					m.[versao_registro] as version_register, 
					te.[TerminalType] as terminal_type, 
					ro.id as company_id, 
					ro.name as company_name,
					(case 
						when (m.[Id] <= 20) then 0 
						when (te.[TerminalType]='I' and m.[Id]<=57000) then 0
						when (te.[TerminalType]='C' and m.[Id]<=70000) then 0
						else 1 end) register_enabled
				FROM
						[QhMaster_Sinc] m with (nolock)
						inner join [QhTerminalReg] te with (nolock) on m.[Id]=te.[Id]
						inner join [rooms] ro with (nolock) on te.[Room]=ro.[id]
				WHERE
						isnull(m.[sinc_new],0)=0
						and 1 = (case when isnull(m.[LA],'L')='A' then 1 else 
						--case when (SELECT DATEDIFF(hour, m.[DATE_SINC], getdate()))>0 then 1 else 0 end 
						0 end)
				ORDER BY
						isnull(m.[RdgDateTime],getdate()) ASC;
			end /**End @operation=1*/
		else if(9=@operation)
			begin
				UPDATE 
					[QhMaster_Sinc] 
				SET 
					[sinc_new]=@sinc 
				WHERE
					[moviment_id]=@movement_legacy_id
					AND [versao_registro]=@version_register;
			end
    end
go

/*
declare 
	@operation INT=4,
	@movement_legacy_id BIGINT=1742,
	@sinc BIT=0,
	@version_register TIMESTAMP = 0x0000000000000E8A;

exec pr_sinc_data_product_movement_database @operation, @movement_legacy_id, @sinc, @version_register;
*/