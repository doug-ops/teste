USE [master]
GO

EXEC master.dbo.sp_addlinkedserver @server = N'LinkedtoRDS', @srvproduct=N'', @provider=N'SQLNCLI', @datasrc=N'bmaws.cxzqn3pftnpb.eu-central-1.rds.amazonaws.com';

EXEC master.dbo.sp_addlinkedsrvlogin @rmtsrvname=N'LinkedtoRDS',@useself=N'False',@locallogin=NULL,@rmtuser=N'bmaws',@rmtpassword='bmawselectro';

EXEC master.dbo.sp_addlinkedserver @server = N'LinkedtoRDS', @srvproduct=N'', @provider=N'SQLNCLI', @datasrc=N'bmaws.cxzqn3pftnpb.eu-central-1.rds.amazonaws.com';

EXEC master.dbo.sp_addlinkedsrvlogin @rmtsrvname=N'LinkedtoRDS',@useself=N'False',@locallogin=NULL,@rmtuser=N'bmaws',@rmtpassword='bmawselectro';


select * from [LinkedtoRDS].[REMOTO].dbo.tb_cad_usuario

select *
FROM   openquery
        (
        MYSQL_MANAGER, 
        '
         select * from person
        '
        )

select pm.id, po.codigo_produto, pm.description, po.descricao from MYSQL_MANAGER...product pm 
inner join [LinkedtoRDS].[REMOTO].dbo.tb_cad_produto po on pm.id = po.codigo_produto

EXEC sp_dropserver 'MYSQL', 'droplogins';
EXEC sp_addlinkedserver
   @server = 'MYSQL',
   @srvproduct = 'MySQL',
   @provider = 'MSDASQL',
   @datasrc = 'MYSQL',
   --@location = 'location',
   @provstr = '{MySQL ODBC 8.0 ANSI Driver};54.89.222.132; PORT=3306; DATABASE=manager; USER=bmaws; PASSWORD=BMAWSElectro@123;OPTION=3;'--,
   --@catalog = 'catalog'
   ;
GO


 EXEC master.dbo.sp_addlinkedserver @server = N'MYSQL', @srvproduct=N'MYSQL', @provider=N'MSDASQL', @datasrc=N'MYSQL',
 --@provstr=N'ODBC;DSN=MYSQL'
 @provstr=N'DRIVER=(MySQL ODBC 8.0 ANSI Driver); SERVER=54.89.222.132; PORT=3306; DATABASE=manager; USER=bmaws; PASSWORD=BMAWSElectro@123;';

  EXEC master.dbo.sp_addlinkedsrvlogin @rmtsrvname=N'MYSQL',@useself=N'False',@locallogin=NULL,@rmtuser=NULL,@rmtpassword=NULL

EXEC master.dbo.sp_addlinkedserver @server = N'MYSQL', @srvproduct=N'MySQL', @provider=N'MSDASQL', 
@provstr=N'DRIVER=(MySQL ODBC 5.2 ANSI Driver);SERVER=54.89.222.132;PORT=3306;DATABASE=manager; USER=bmaws;PASSWORD=BMAWSElectro@123;OPTION=3;';

EXEC master.dbo.sp_addlinkedsrvlogin @rmtsrvname=N'MYSQL',@useself=N'False',@locallogin=NULL,@rmtuser=N'bmaws',@rmtpassword='BMAWSElectro@123';

EXEC master.dbo.sp_serveroption @server=N'MYSQL', @optname=N'rpc', @optvalue=N'true'
GO
EXEC master.dbo.sp_serveroption @server=N'MYSQL', @optname=N'rpc out', @optvalue=N'true'
GO

